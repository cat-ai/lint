package io.cat.ai.lint.concurrent.lint.backend

import java.util
import java.util.concurrent.locks.Lock

import io.cat.ai.lint._
import io.cat.ai.lint.concurrent._
import io.cat.ai.lint.concurrent.alias.Operation
import io.cat.ai.lint.concurrent.atomic.Implicits._
import io.cat.ai.lint.concurrent.lint._
import io.cat.ai.lint.concurrent.locks.{SuspendableLock, SuspendableLockMonitor}

import scala.language.postfixOps

class DefaultLintBackend(override val mode: Mode,
                         override val policy: ProducerConsumerPolicy,
                         override val availableProcessorsToJVM: Int,
                         override val nFactor: Int = 100)
                        (implicit abstractLintExecutorBackend: AbstractLintExecutorBackend) extends LintBackend {

  override final val suspendableLock: Lock with SuspendableLock = SuspendableLockMonitor()

  override final val operationQueue: util.Queue[Operation] = concurrent.queue.ConcurrentQueueFactory[Operation](policy, maxOperationsCount)

  override def maxOperationsCount: Int = availableProcessorsToJVM * nFactor

  override def executorBackend: AbstractLintExecutorBackend = abstractLintExecutorBackend

  @inline private def isCurrentThreadInterrupted: Boolean = Thread.currentThread.isInterrupted

  protected [backend] val finiteConditionalOperation: Operation = () => {
    while(!isCurrentThreadInterrupted) {
      val operation = operationQueue poll

      if (operation ne null)
        operation run()
      else {
        suspendableLock lock()
        try
          suspendableLock waitForWakeUp()
        catch {
          case _: InterruptedException => Thread.currentThread().interrupt()
        }
        finally suspendableLock unlock()
      }
    }
  }

  def submit(operation: Operation): Unit =
    if (executorBackend.threads.size < availableProcessorsToJVM)
      if (operation ne null)
        executorBackend execute(operation, finiteConditionalOperation)
    else {
        mode match {
          case FIFO => operationQueue add operation
          case LIFO => /*TODO: implement*/ ???
        }
        suspendableLock lock()
        suspendableLock wakeUpWaiting()
        suspendableLock unlock()
      }

  def submitBatch(operations: Operation*): Unit = ???

  override def stop(): Unit = {
    for (thread <- executorBackend.threads)
      thread interrupt()

    stopped := true
  }

  override def isStopped: Boolean = is(stopped)
}
