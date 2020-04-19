package fpge

import cats.effect.{ Resource, Sync }
import com.badlogic.gdx
import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import fpge.settings.ApplicationConfig

class Application(implementation: gdx.Application, val graphics: Graphics, val audio: Audio, val input: Input) {}

object Application {

  def create[F[_]: Sync](listener: gdx.ApplicationListener, config: ApplicationConfig): Resource[F, Application] =
    Resource
      .make {
        Sync[F].delay(new LwjglApplication(listener, config.toGDXConfig))
      } { application =>
        Sync[F].delay(application.exit())
      }
      .map { application =>
        new Application(
          implementation = application,
          graphics       = new Graphics(application.getGraphics),
          audio          = new Audio(application.getAudio),
          input          = new Input(application.getInput)
        )
      }
}
