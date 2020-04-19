package fpge.events

import com.badlogic.gdx.ApplicationListener
import eu.timepit.refined.auto._
import fpge.ADT
import fpge.settings.{ Height, Resolution, Width }

sealed trait AppEvent extends ADT

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
