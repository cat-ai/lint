package io.cat.ai.lint.concurrent.locks

trait SuspendableLock {

  def waitForWakeUp(): Unit

  def wakeUpWaiting(): Unit
}