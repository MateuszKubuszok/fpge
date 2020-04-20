package fpge

import cats.effect.Resource
import com.badlogic.gdx
import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.Gdx
import fpge.events.{ EventBus, InputEvent, WindowEvent }
import fpge.settings.ApplicationConfig
import monix.catnap.MVar
import monix.eval.{ Coeval, Task }

import scala.concurrent.duration._

class Application(implementation: gdx.Application, val graphics: Graphics, val audio: Audio, val input: Input) {}

object Application {

  @SuppressWarnings(Array("org.wartremover.warts.Equals", "org.wartremover.warts.Null", "org.wartremover.warts.While"))
  def create(
    config:          ApplicationConfig,
    eventBus:        EventBus,
    applicationMVar: MVar[Task, Application],
    redraw:          Coeval[Unit]
  ): Resource[Task, Application] =
    Resource
      .make {
        Task
          .delay {
            Gdx.app = null // scalastyle:ignore
            val fireAndForget =
              new Thread(() => {
                new LwjglApplication(WindowEvent.listener(eventBus, redraw), config.toGDXConfig)
                ()
              })
            fireAndForget.setDaemon(true)
            fireAndForget.start()
          }
          .flatMap { _ =>
            Task.tailRecM(Option(Gdx.app)) {
              case Some(application) => Task.pure(Right(application))
              case None              => Task.sleep(50.millis).map(_ => Left(Option(Gdx.app)))
            }
          }
      }(application => Task.delay(application.exit()))
      .evalMap { application =>
        application.getInput.setInputProcessor(InputEvent.listener(eventBus))
        val wrapper = new Application(
          implementation = application,
          graphics       = new Graphics(application.getGraphics),
          audio          = new Audio(application.getAudio),
          input          = new Input(application.getInput)
        )
        applicationMVar.put(wrapper).map(_ => wrapper)
      }
}
