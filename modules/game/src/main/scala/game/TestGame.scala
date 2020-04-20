package game

import fpge.{ Application, GameApp }
import fpge.events.{ InputEvent, WindowEvent }
import monix.eval.{ Coeval, Task }

object TestGame extends GameApp[Unit, Unit] {

  override def parseArguments(args: List[String]): Task[Unit] = Task(println("arguments parsed"))

  override def initialGameState: Unit = ()

  override def processWindowEvent(application: Application, gameState: Unit, windowEvent: WindowEvent): Unit =
    println(windowEvent)

  override def processInputEvent(application: Application, gameState: Unit, inputEvent: InputEvent): Unit =
    println(inputEvent)

  override def render(application: Application, gameState: Unit): Coeval[Unit] = Coeval.unit
}
