package game

import fpge.{ Application, GameApp }
import fpge.events.{ InputEvent, WindowEvent }
import monix.eval.Task

object Run extends GameApp[Unit, Unit] {

  override def parseArguments(args: List[String]): Task[Unit] = Task(println("arguments parsed"))

  override def initialGameState: Unit = ()

  override def processWindowEvent(application: Application, gameState: Unit, windowEvent: WindowEvent): Task[Unit] =
    Task(println(windowEvent))

  override def processInputEvent(application: Application, gameState: Unit, inputEvent: InputEvent): Task[Unit] =
    Task(println(inputEvent))
}
