package fpge

import fpge.events.{ InputEvent, WindowEvent }
import monix.eval.{ Coeval, Task }

trait GameLogic[GameConfig, GameState] {

  def parseArguments(args: List[String]): Task[GameConfig] // TODO use library

  // should use arguments and provide some extensibility to save/load I guess?
  def initialGameState: GameState

  def processInputEvent(application: Application, gameState: GameState, inputEvent: InputEvent): GameState

  def processWindowEvent(application: Application, gameState: GameState, windowEvent: WindowEvent): GameState

  def render(application: Application, gameState: GameState): Coeval[Unit]
}
