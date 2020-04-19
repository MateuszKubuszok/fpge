package game

import fpge.{ Application, GameApp }
import fpge.events.WindowEvent
import monix.eval.Task

object Run extends GameApp[Unit, Unit] {

  override def parseArguments(args: List[String]): Task[Unit] = Task.unit

  override def initialGameState: Unit = ()

  override def processWindowEvent(application: Application, gameState: Unit, windowEvent: WindowEvent): Task[Unit] =
    Task.unit
}
