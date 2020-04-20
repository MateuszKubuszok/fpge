package game

import fpge.{ Application, GameApp, GameLogic }
import fpge.events.{ InputEvent, WindowEvent }
import monix.eval.{ Coeval, Task }

object TestGame
    extends GameApp[Unit, Unit](new GameLogic[Unit, Unit] {

      override def parseArguments(args: List[String]): Task[Unit] = Task(println("arguments parsed"))

      override def initialGameState: Unit = ()

      override def processWindowEvent(application: Application, gameState: Unit, windowEvent: WindowEvent): Unit = {
        println("start processing window event")
        Thread.sleep(200)
        println(windowEvent)
      }

      override def processInputEvent(application: Application, gameState: Unit, inputEvent: InputEvent): Unit = {
        println("start processing input event")
        Thread.sleep(200)
        println(inputEvent)
      }

      override def render(application: Application, gameState: Unit): Coeval[Unit] = Coeval {
        Thread.sleep(200)
        println("rendered finished")
      }
    })
