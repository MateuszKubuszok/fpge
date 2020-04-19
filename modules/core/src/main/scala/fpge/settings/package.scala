package fpge

import eu.timepit.refined.api.Refined
import eu.timepit.refined.collection.NonEmpty
import eu.timepit.refined.numeric.Positive
import eu.timepit.refined.pureconfig._
import io.estatico.newtype.ops._
import io.estatico.newtype.macros.newtype
import pureconfig.ConfigReader

package object settings {

  @newtype case class Title(value: String Refined NonEmpty)
  object Title {
    implicit val configReader: ConfigReader[Title] = ConfigReader[String Refined NonEmpty].coerce
  }

  @newtype case class Height(value: Int Refined Positive)
  object Height {
    implicit val configReader: ConfigReader[Height] = ConfigReader[Int Refined Positive].coerce
  }
  @newtype case class Width(value: Int Refined Positive)
  object Width {
    implicit val configReader: ConfigReader[Width] = ConfigReader[Int Refined Positive].coerce
  }

  @newtype case class MaxNetworkThreads(value: Int Refined Positive)
  object MaxNetworkThreads {
    implicit val configReader: ConfigReader[MaxNetworkThreads] = ConfigReader[Int Refined Positive].coerce
  }

  @newtype case class OpenGL30(value: Boolean)
  object OpenGL30 {
    implicit val configReader: ConfigReader[OpenGL30] = ConfigReader[Boolean].coerce
  }
  @newtype case class FullScreen(value: Boolean)
  object FullScreen {
    implicit val configReader: ConfigReader[FullScreen] = ConfigReader[Boolean].coerce
  }
  @newtype case class VSync(value: Boolean)
  object VSync {
    implicit val configReader: ConfigReader[VSync] = ConfigReader[Boolean].coerce
  }
}
