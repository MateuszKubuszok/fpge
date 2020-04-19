package fpge.settings

import cats.effect.Sync
import cats.syntax.flatMap._
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import com.typesafe.config.{ Config, ConfigFactory }
import pureconfig._
import pureconfig.generic.auto._
import eu.timepit.refined.pureconfig._

final case class ApplicationConfig(
  title:             Title,
  size:              Size,
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

  def default[F[_]: Sync]: F[ApplicationConfig] = Sync[F].delay(ConfigFactory.defaultApplication()).flatMap(load[F](_))

  def load[F[_]: Sync](config: Config): F[ApplicationConfig] = Sync[F].delay {
    ConfigSource.fromConfig(config).load[ApplicationConfig]
  }
}
