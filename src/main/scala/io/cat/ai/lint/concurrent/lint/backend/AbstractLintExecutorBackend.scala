package io.cat.ai.lint.concurrent.lint.backend

import java.util.concurrent.atomic.AtomicBoolean

import io.cat.ai.lint.concurrent.atomic.FastAtomicLong
import io.cat.ai.lint.concurrent.lint.LintThread
import io.cat.ai.lint.control.Starter
import io.cat.ai.lint.concurrent.alias.Operation

import scala.collection.mutable
import scala.language.postfixOps

import AbstractLintExecutorBackend._

/**
  * Contract is responsible for executing operations
  *
  * It is effectively a bridge between the LintThread and a LintBackend
  * */
protected[lint] trait LintExecutorBackend {

  def threads: mutable.Set[_ <: Thread]

  def stopped: AtomicBoolean

  def runningOperations: Int

  def execute(operation: Operation): Unit

  def execute(operation: Operation, operation2: Operation): Unit

  def execute(operation: Operation, operations: Seq[Operation]): Unit
}

protected[lint] abstract class AbstractLintExecutorBackend protected[backend] (lintThreads: mutable.Set[Thread]) extends LintExecutorBackend with Starter[Thread, Unit] {

  override def threads: mutable.Set[Thread] = lintThreads

  override def start(a: Thread): Unit
}

object AbstractLintExecutorBackend {

  protected[concurrent] val defaultName: String = "Lint-Thread"

  protected[concurrent] val idLong: FastAtomicLong = new FastAtomicLong

  def apply(): AbstractLintExecutorBackend = new LintExecutorBackendImpl(new mutable.HashSet[Thread]())

  def apply(operation: Operation): AbstractLintExecutorBackend with Starter[Thread, Unit] = {

    val executorBackend = new LintExecutorBackendImpl(new mutable.HashSet[Thread]())

    executorBackend execute operation

    executorBackend
  }
}

protected[lint] final class LintExecutorBackendImpl protected[backend] (lintThreads: mutable.Set[Thread]) extends AbstractLintExecutorBackend(lintThreads) {

  override val stopped: AtomicBoolean = new AtomicBoolean(false)

  override def execute(operation: Operation): Unit = addAndStart {
    new LintThread(operation) {

      setName(s"$defaultName-${idLong.incrementAndGet}")

      override def run(): Unit = operation run()
    }
  }

  override def execute(operation: Operation, operation2: Operation): Unit = addAndStart {
    new LintThread(operation) {

      setName(s"$defaultName-${idLong.incrementAndGet}")

      override def run(): Unit = {
        operation run()
        operation2 run()
      }
    }
  }

  override def execute(operation: Operation, operations: Seq[Operation]): Unit = runOperations(operation, operations)

  private def runOperations(operation: Operation, operations: Seq[Operation]): Unit = addAndStart {
    new LintThread(operation) {

      setName(s"$defaultName-${idLong.incrementAndGet}")

      override def run(): Unit = for (op <- operations :+ operation) op run()
    }
  }

  private def addAndStart(thread: Thread): Unit = {
    threads += thread
    start(thread)
  }

  override def start(thread: Thread): Unit = thread start()

  override def runningOperations: Int = threads count(_.getState == Thread.State.RUNNABLE)
}