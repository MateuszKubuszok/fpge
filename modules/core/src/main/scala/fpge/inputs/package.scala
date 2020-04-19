package fpge

import eu.timepit.refined.api.Refined
import eu.timepit.refined.numeric.NonNegative
import io.estatico.newtype.macros.newtype

package object inputs {

  @newtype case class X(int: Int Refined NonNegative)
  @newtype case class Y(int: Int Refined NonNegative)
}
