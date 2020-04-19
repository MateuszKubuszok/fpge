package fpge

import cats.effect.Resource
import com.badlogic.gdx
import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import fpge.events.{ EventBus, InputEvent, WindowEvent }
import fpge.settings.ApplicationConfig
import monix.eval.Task

class Application(implementation: gdx.Application, val graphics: Graphics, val audio: Audio, val input: Input) {
}

object Application {

  def create(config: ApplicationConfig, eventBus: EventBus): Resource[Task, Application] =
    Resource
      .make {
        Task.delay(new LwjglApplication(WindowEvent.listener(eventBus), config.toGDXConfig))
      } { application =>
        Task.delay(application.exit())
      }
      .map { application =>
        application.getInput.setInputProcessor(InputEvent.listener(eventBus))
        new Application(
          implementation = application,
          graphics       = new Graphics(application.getGraphics),
          audio          = new Audio(application.getAudio),
          input          = new Input(application.getInput)
        )
      }
}
