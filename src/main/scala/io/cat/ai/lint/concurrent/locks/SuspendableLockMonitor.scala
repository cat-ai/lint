package io.cat.ai.lint.concurrent.locks

import java.util.concurrent.TimeUnit
import java.util.concurrent.locks.{Condition, ReentrantLock}

trait SuspendableLockMonitor extends ReentrantLock with SuspendableLock

object SuspendableLockMonitor {

  def apply(): SuspendableLockMonitor = new SuspendableLockMonitor {

    private val condition: Condition = super.newCondition()

    override def lock(): Unit = super.lock()

    override def lockInterruptibly(): Unit = super.lockInterruptibly()

    override def tryLock(): Boolean = super.tryLock()

    override def tryLock(time: Long, unit: TimeUnit): Boolean = super.tryLock(time, unit)

    override def unlock(): Unit = super.unlock()

    override def newCondition(): Condition = condition

    override def waitForWakeUp: Unit = condition.await()

    override def wakeUpWaiting: Unit = condition.signal()
  }
}