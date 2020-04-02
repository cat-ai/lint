package io.cat.ai.lint.concurrent.lint.backend

import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.locks.Lock

import io.cat.ai.lint.concurrent.ProducerConsumerPolicy
import io.cat.ai.lint.concurrent.alias.Operation
import io.cat.ai.lint.concurrent.lint.Mode
import io.cat.ai.lint.concurrent.locks.SuspendableLock
import io.cat.ai.lint.control.Stoppable

import scala.language.postfixOps

trait LintBackend extends Stoppable {

  def availableProcessorsToJVM: Int

  def maxOperationsCount: Int

  def operationQueue: java.util.Queue[Operation]

  def nFactor: Int

  def executorBackend: AbstractLintExecutorBackend

  def mode: Mode

  def policy: ProducerConsumerPolicy

  def suspendableLock: Lock with SuspendableLock

  def stopped: AtomicBoolean = executorBackend.stopped

  def submit(operation: Operation): Unit

  def submitBatch(operation: Operation*): Unit
}