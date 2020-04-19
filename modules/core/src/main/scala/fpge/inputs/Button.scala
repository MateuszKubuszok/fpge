package fpge.inputs

import enumeratum._

sealed abstract class Button(val id: Int) extends EnumEntry
object Button extends Enum[Button] {
  // scalastyle:off magic.number
  case object LEFT extends Button(0)
  case object RIGHT extends Button(1)
  case object MIDDLE extends Button(2)
  case object BACK extends Button(3)
  case object FORWARD extends Button(4)
  // scalastyle:on magic.number

  override def values: IndexedSeq[Button] = findValues

  val fromGDX: Int => Button = values.map(b => b.id -> b).toMap
}
