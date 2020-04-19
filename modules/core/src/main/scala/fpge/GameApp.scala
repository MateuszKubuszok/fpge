package fpge

import cats.effect.ExitCode
import fpge.events.{ AppEvent, EventBus, InputEvent, WindowEvent }
import fpge.settings.ApplicationConfig
import monix.catnap.MVar
import monix.eval.{ Task, TaskApp }
import monix.execution.Scheduler

trait GameApp[GameConfig, GameState] extends TaskApp {
  // TODO: parse arguments

  final override def run(args: List[String]): Task[ExitCode] =
    for {
      _ <- parseArguments(args) // TODO
      config <- loadConfig
      eventBus <- createEventBus
      gameStateMVar <- MVar.of[Task, GameState](initialGameState)
      _ <- Application.create(config, eventBus).use { application =>
        eventBus.subscription
          .evalMap { event =>
            for {
              state <- gameStateMVar.read
              newState <- processAppEvent(application, state, event)
              _ <- gameStateMVar.put(newState)
            } yield ()
          }
          .compile
          .drain
      }
    } yield ExitCode.Success

  def loadConfig: Task[ApplicationConfig] = ApplicationConfig.default

  def createEventBus: Task[EventBus] = EventBus.create(Scheduler.singleThread("event-publisher"))

  def processAppEvent(application: Application, gameState: GameState, appEvent: AppEvent): Task[GameState] =
    appEvent match {
      case inputEvent:  InputEvent  => processInputEvent(application, gameState, inputEvent)
      case windowEvent: WindowEvent => processWindowEvent(application, gameState, windowEvent)
    }

  def parseArguments(args: List[String]): Task[GameConfig] // TODO use library

  def initialGameState: GameState

  def processInputEvent(application: Application, gameState: GameState, inputEvent: InputEvent): Task[GameState]

  def processWindowEvent(application: Application, gameState: GameState, windowEvent: WindowEvent): Task[GameState]
}
