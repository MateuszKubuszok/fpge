package fpge.events

import fs2.concurrent.{ NoneTerminatedQueue, Queue }
import monix.eval.Task
import monix.execution.Scheduler

class EventBus(queue: NoneTerminatedQueue[Task, AppEvent], scheduler: Scheduler) {

  def publish(event: => AppEvent): Unit =
    Task(event)
      .flatMap {
        case WindowEvent.ExitRequested => queue.enqueue1(None)
        case event                     => queue.enqueue1(Some(event))
      }
      .runAsyncAndForget(scheduler)

  def subscription: fs2.Stream[Task, AppEvent] = queue.dequeue
}

object EventBus {

  def create(eventScheduler: Scheduler): Task[EventBus] =
    Queue.noneTerminated[Task, AppEvent].map(new EventBus(_, eventScheduler))
}
