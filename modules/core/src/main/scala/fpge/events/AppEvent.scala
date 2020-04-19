package fpge.events

import com.badlogic.gdx.{ ApplicationListener, InputProcessor }
import eu.timepit.refined.auto._
import fpge.{ inputs, ADT }
import fpge.inputs.{ Button, Key, Position, ScrollingDirection, X, Y }
import fpge.settings.{ Height, Resolution, Width }

sealed trait AppEvent extends ADT

sealed trait InputEvent extends AppEvent
object InputEvent {
  final case class KeyPressed(key:         Key) extends InputEvent
  final case class KeyReleased(key:        Key) extends InputEvent
  final case class CharTyped(char:         Char) extends InputEvent
  final case class TouchPressed(position:  Position, button: Button) extends InputEvent
  final case class TouchReleased(position: Position, button: Button) extends InputEvent
  final case class TouchDragged(position:  Position) extends InputEvent
  final case class MouseMoved(position:    Position) extends InputEvent
  final case class Scrolled(direction:     ScrollingDirection) extends InputEvent

  def listener(bus: EventBus): InputProcessor = new InputProcessor {
    def keyDown(keycode:    Int): Boolean = { bus.publish(KeyPressed(Key.fromGDX(keycode))); true }
    def keyUp(keycode:      Int): Boolean = { bus.publish(KeyReleased(Key.fromGDX(keycode))); true }
    def keyTyped(character: Char): Boolean = { bus.publish(CharTyped(character)); true }
    def touchDown(screenX:  Int, screenY: Int, pointer: Int, button: Int): Boolean = {
      bus.publish(TouchPressed(Position(X(screenX), Y(screenY)), Button.fromGDX(button))); true
    }
    def touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean = {
      bus.publish(TouchReleased(Position(X(screenX), Y(screenY)), Button.fromGDX(button))); true
    }
    def touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean = {
      bus.publish(TouchDragged(Position(X(screenX), Y(screenY)))); true
    }
    def mouseMoved(screenX: Int, screenY: Int): Boolean = {
      bus.publish(MouseMoved(Position(X(screenX), Y(screenY)))); true
    }
    def scrolled(amount: Int): Boolean = {
      bus.publish(Scrolled(if (amount >= 0) ScrollingDirection.UP else ScrollingDirection.DOWN)); true
    }
  }
}

sealed trait WindowEvent extends AppEvent
object WindowEvent {
  case object Created extends WindowEvent
  final case class Resized(resolution: Resolution) extends WindowEvent
  case object RenderRequested extends WindowEvent
  case object PauseRequested extends WindowEvent
  case object ResumeRequested extends WindowEvent
  case object ExitRequested extends WindowEvent

  def listener(bus: EventBus): ApplicationListener =
    new ApplicationListener {
      def create(): Unit = bus.publish(Created)
      def resize(width: Int, height: Int): Unit = bus.publish(Resized(Resolution(Width(width), Height(height))))
      def render():  Unit = bus.publish(RenderRequested)
      def pause():   Unit = bus.publish(PauseRequested)
      def resume():  Unit = bus.publish(ResumeRequested)
      def dispose(): Unit = bus.publish(ExitRequested)
    }
}
