package io.cat.ai.lint.concurrent.lint.backend

import java.util.concurrent.atomic.AtomicBoolean

import io.cat.ai.lint.concurrent.atomic.FastAtomicLong
import io.cat.ai.lint.concurrent.lint.LintThread
import io.cat.ai.lint.control.Starter

import scala.collection.mutable

import AbstractLintExecutorBackend._

import scala.language.postfixOps

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

  def execute(operation: Operation, operations: Operation*): Unit
}

protected[lint] abstract class AbstractLintExecutorBackend protected[backend] (lintThreads: mutable.Set[Thread]) extends LintExecutorBackend with Starter[Thread, Unit] {

  override def threads: mutable.Set[Thread] = lintThreads

  override def start(a: Thread): Unit
}

object AbstractLintExecutorBackend {

  type Operation = Runnable

  protected[concurrent] val defName: String = "Lint-Thread"

  protected[concurrent] val idLong: FastAtomicLong = new FastAtomicLong

  def apply(threadsName: String = ""): AbstractLintExecutorBackend = new LintExecutorBackendImpl(new mutable.HashSet[Thread]())
}

protected[lint] final class LintExecutorBackendImpl protected[backend] (lintThreads: mutable.Set[Thread]) extends AbstractLintExecutorBackend(lintThreads) {

  override val stopped: AtomicBoolean = new AtomicBoolean(false)

  override def execute(operation: Operation): Unit = {
    val lintThread: LintThread = new LintThread(operation) {

      setName(s"$defName-${idLong.incrementAndGet}")

      override def run(): Unit = {
        operation run()
      }
    }
    threads += lintThread

    start(lintThread)
  }

  override def execute(operation: Operation, operation2: Operation): Unit = {
    val lintThread: LintThread = new LintThread(operation) {

      setName(s"$defName-${idLong.incrementAndGet}")

      override def run(): Unit = {
        operation run()
        operation2 run()
      }
    }
    threads += lintThread

    start(lintThread)
  }

  override def execute(operation: Operation, operations: Operation*): Unit = {
    runOperations(operation, operations)
  }

  private def runOperations(operation: Operation, operations: Seq[Operation]): Unit = {
    val lintThread: LintThread = new LintThread(operation) {

      setName(s"$defName-${idLong.incrementAndGet}")

      override def run(): Unit = {
        operation run()
        operations foreach { _ run() }
      }
    }
    threads += lintThread

    start(lintThread)
  }

  override def start(thread: Thread): Unit = thread start()

  override def runningOperations: Int = threads count(_.getState == Thread.State.RUNNABLE)
}