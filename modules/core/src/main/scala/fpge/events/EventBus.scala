package fpge.events

import cats.syntax.functor._
import fs2.concurrent.{ NoneTerminatedQueue, Queue }
import monix.eval.Task
import monix.execution.Scheduler

import scala.concurrent.duration._

class EventBus(queue: NoneTerminatedQueue[Task, AppEvent], scheduler: Scheduler) {

  private def terminateQueue = queue.enqueue1(None)
  private def enqueueIfSpace(event: AppEvent) =
    queue.offer1(Some(event)).timeout(1.millis).onErrorHandle(_ => false).void
  private def enqueueIfFast(event:  AppEvent) = queue.enqueue1(Some(event)).timeout(10.millis).onErrorHandle(_ => ())
  private def ensureEnqueued(event: AppEvent) = queue.enqueue1(Some(event))

  def publish(event: => AppEvent): Unit =
    Task(event)
      .flatMap {
        case WindowEvent.ExitRequested => terminateQueue
        case mouseMoved:   InputEvent.MouseMoved   => enqueueIfSpace(mouseMoved)
        case touchDragged: InputEvent.TouchDragged => enqueueIfSpace(touchDragged)
        case inputEvent:   InputEvent              => enqueueIfFast(inputEvent)
        case event => ensureEnqueued(event)
      }
      .runAsyncAndForget(scheduler)

  def subscription: fs2.Stream[Task, AppEvent] = queue.dequeue
}

object EventBus {

  def create(eventScheduler: Scheduler): Task[EventBus] =
    Queue.boundedNoneTerminated[Task, AppEvent](3).map(new EventBus(_, eventScheduler))
}
