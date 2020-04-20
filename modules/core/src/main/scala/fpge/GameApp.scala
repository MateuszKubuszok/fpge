package fpge

import cats.effect.ExitCode
import cats.effect.concurrent.Ref
import cats.implicits._
import fpge.events.{ AppEvent, EventBus, InputEvent, WindowEvent }
import fpge.settings.ApplicationConfig
import monix.catnap.MVar
import monix.eval.{ Coeval, Task, TaskApp }
import monix.execution.Scheduler
import monix.execution.atomic.PaddingStrategy

trait GameApp[GameConfig, GameState] extends TaskApp {
  // TODO: parse arguments
  final override def run(args: List[String]): Task[ExitCode] =
    for {
      _ <- parseArguments(args) // TODO
      config <- loadConfig
      eventBus <- createEventBus
      gameStateRef <- Ref.of(initialGameState)
      applicationMVal <- MVar.empty[Task, Application](PaddingStrategy.NoPadding)
      // TODO: pass MVar or whatever we use there
      _ <- Application.create(config, eventBus, applicationMVal, redraw(applicationMVal, gameStateRef)).use {
        application =>
          eventBus.subscription
            .evalMap(event => gameStateRef.update(processAppEvent(application, _, event)))
            .compile
            .drain
      }
    } yield ExitCode.Success

  def loadConfig: Task[ApplicationConfig] = ApplicationConfig.default

  def createEventBus: Task[EventBus] = EventBus.create(Scheduler.singleThread("event-publisher"))

  def redraw(applicationMVar: MVar[Task, Application], gameStateRef: Ref[Task, GameState]): Coeval[Unit] = {
    implicit val renderingScheduler: Scheduler = Scheduler.io("renderer", daemonic = true)
    Coeval((applicationMVar.tryRead, gameStateRef.get).parTupled.runSyncUnsafe()).flatMap {
      case (Some(application), gameState) => render(application, gameState)
      case (None, _)                      => Coeval.unit
    }
  }

  def processAppEvent(application: Application, gameState: GameState, appEvent: AppEvent): GameState =
    appEvent match {
      case inputEvent:  InputEvent  => processInputEvent(application, gameState, inputEvent)
      case windowEvent: WindowEvent => processWindowEvent(application, gameState, windowEvent)
    }

  def parseArguments(args: List[String]): Task[GameConfig] // TODO use library

  def initialGameState: GameState

  def processInputEvent(application: Application, gameState: GameState, inputEvent: InputEvent): GameState

  def processWindowEvent(application: Application, gameState: GameState, windowEvent: WindowEvent): GameState

  def render(application: Application, gameState: GameState): Coeval[Unit]
}
