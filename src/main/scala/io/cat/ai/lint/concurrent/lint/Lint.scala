package io.cat.ai.lint.concurrent.lint

import io.cat.ai.lint.concurrent._
import io.cat.ai.lint.concurrent.alias.Operation
import io.cat.ai.lint.concurrent.lint.backend._
import io.cat.ai.lint.control.{Starter, Stoppable}

import scala.language.postfixOps

/**
  * Lint is a contract for achieving concurrency of execution one or more operations
  *
  * Lint uses [[LintBackend]] as manager that submits operations and then executes with [[LintExecutorBackend]] based on [[Mode]] principle
  *
  * Operations are submitted to the Lint via a queue [[LintBackend.operationQueue]] that depends on policy [[LintBackend.policy]], which holds one or more operations than thread
  **/
trait Lint extends Starter[Operation, Unit] with Stoppable {

  implicit def backend: LintBackend

  override def stop(): Unit = backend stop()

  override def isStopped: Boolean = backend isStopped

  def -~>(operation: Operation): Lint

  def *-~>(operations: Operation*): Lint

  def activeOperations: Int
}

private class LintImpl(implicit lintBackend: LintBackend) extends Lint { self =>

  override implicit def backend: LintBackend = lintBackend

  override def -~>(operation: Operation): Lint = {
    backend submit operation
    self
  }

  override def *-~>(operations: Operation*): Lint = {
    backend submitBatch(operations:_*)
    self
  }

  override def start(a: Operation): Unit = self -~> a

  override def activeOperations: Int = backend.executorBackend.runningOperations
}

object Lint {

  private lazy val procs: Int = sys.runtime.availableProcessors
  private lazy val nFactor: Int = 100

  def apply()(implicit lintBackend: LintBackend): Lint = new LintImpl

  def apply(operation: Operation)(implicit lintBackend: LintBackend): Lint = {
    val lint = new LintImpl
    lint start operation

    lint
  }

  def apply(mode: Mode, availableProcessors: Int, nFactor: Int): Lint = new LintImpl()(Implicits.lintBackend(mode, availableProcessors, nFactor))

  def apply(mode: Mode = FIFO, policy: ProducerConsumerPolicy = SingleProducerSingleConsumer): Lint = new LintImpl()(Implicits.lintBackend(mode, policy, procs, nFactor))
}