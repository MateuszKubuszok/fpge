package fpge.settings

import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import com.typesafe.config.{ Config, ConfigFactory }
import pureconfig._
import pureconfig.generic.auto._
import eu.timepit.refined.pureconfig._
import monix.eval.Task

final case class ApplicationConfig(
                                    title:             Title,
                                    size:              Resolution,
                                    fullScreen:        FullScreen,
                                    openGL30:          OpenGL30,
                                    maxNetworkThreads: MaxNetworkThreads
) {

  private[fpge] def toGDXConfig: LwjglApplicationConfiguration = {
    val cfg = new LwjglApplicationConfiguration()
    cfg.title         = title.value.value
    cfg.width         = size.width.value.value
    cfg.height        = size.height.value.value
    cfg.fullscreen    = fullScreen.value
    cfg.useGL30       = openGL30.value
    cfg.maxNetThreads = maxNetworkThreads.value.value
    cfg
  }
}

object ApplicationConfig {

  def default: Task[ApplicationConfig] = Task(ConfigFactory.defaultApplication).flatMap(load)

  def load(config: Config): Task[ApplicationConfig] = Task(
    ConfigSource.fromConfig(config).loadOrThrow[ApplicationConfig]
  )
}
