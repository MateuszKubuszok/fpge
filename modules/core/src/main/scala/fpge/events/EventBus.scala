package fpge.events

import fs2.concurrent.Queue
import monix.eval.Task
import monix.execution.Scheduler

class EventBus(queue: Queue[Task, AppEvent], scheduler: Scheduler) {

  def publish(event: => AppEvent): Unit = Task(event).flatMap(queue.enqueue1).runAsyncAndForget(scheduler)

  def subscription: fs2.Stream[Task, AppEvent] = queue.dequeue
}

object EventBus {

  def create(eventScheduler: Scheduler): Task[EventBus] =
    Queue.unbounded[Task, AppEvent].map(new EventBus(_, eventScheduler))
}
