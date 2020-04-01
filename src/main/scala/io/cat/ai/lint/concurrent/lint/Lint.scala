package io.cat.ai.lint.concurrent.lint

import io.cat.ai.lint.concurrent.lint.backend.LintBackend
import io.cat.ai.lint.control.{Starter, Stoppable}

import scala.language.postfixOps

trait Lint extends Starter[Runnable, Unit] with Stoppable {

  implicit def backend: LintBackend

  override def stop(): Unit = backend.stop()

  override def isStopped: Boolean = backend isStopped

  def -~>(operation: Runnable): Lint

  def *-~>(operations: Runnable*): Lint

  def name: String

  def activeOperations: Int
}

private class LintImpl (override val name: String = "")
                       (implicit lintBackend: LintBackend) extends Lint { self =>

  override implicit def backend: LintBackend = lintBackend

  override def -~>(operation: Runnable): Lint = {
    backend submit operation
    self
  }

  override def *-~>(operations: Runnable*): Lint = {
    backend submitBatch(operations:_*)
    self
  }

  override def start(a: Runnable): Unit = self -~> a

  override def activeOperations: Int = backend.executorBackend.runningOperations
}

object Lint {

  def apply(operation: Runnable)(implicit lintBackend: LintBackend): Lint = {
    val lint = new LintImpl()
    lint start operation

    lint
  }

  def apply(threadsName: String = "")(implicit lintBackend: LintBackend): Lint = new LintImpl(threadsName)
}