package fpge

import eu.timepit.refined.api.Refined
import eu.timepit.refined.collection.NonEmpty
import eu.timepit.refined.numeric.Positive
import io.estatico.newtype.macros.newtype

package object settings {

  @newtype case class Title(value: String Refined NonEmpty)

  @newtype case class Height(value: Int Refined Positive)
  @newtype case class Width(value:  Int Refined Positive)

  @newtype case class MaxNetworkThreads(value: Int Refined Positive)

  @newtype case class OpenGL30(value:   Boolean)
  @newtype case class FullScreen(value: Boolean)
  @newtype case class VSync(value:      Boolean)
}
