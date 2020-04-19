package fpge.inputs

import fpge.ADT

sealed trait ScrollingDirection extends ADT
object ScrollingDirection {
  final case object UP extends ScrollingDirection
  final case object DOWN extends ScrollingDirection
}
