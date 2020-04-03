package io.cat.ai.lint.concurrent.lint

import io.cat.ai.lint.concurrent._
import io.cat.ai.lint.concurrent.alias.Operation

import scala.language.implicitConversions

package object backend {

  protected [concurrent] trait BackendImplicits {

    private val priorityPolicy: ProducerConsumerPolicy = SingleProducerSingleConsumer

    implicit def lintExecutorBackend: AbstractLintExecutorBackend = AbstractLintExecutorBackend()

    implicit def lintExecutorBackend(operation: Operation): AbstractLintExecutorBackend = AbstractLintExecutorBackend(operation)

    implicit def defaultLintBackend: LintBackend = new DefaultLintBackend(FIFO, priorityPolicy, sys.runtime.availableProcessors)

    implicit def lintBackend(mode: Mode, availableProcessors: Int, nFactor: Int): DefaultLintBackend = new DefaultLintBackend(mode, priorityPolicy, availableProcessors, nFactor)

    implicit def lintBackend(mode: Mode, policy: ProducerConsumerPolicy, availableProcessors: Int, nFactor: Int): DefaultLintBackend = new DefaultLintBackend(mode, policy, availableProcessors, nFactor)

    implicit def lintBackend(mode: Mode, availableProcessors: Int, nFactor: Int, operation: Operation): DefaultLintBackend =
      new DefaultLintBackend(mode, priorityPolicy, availableProcessors, nFactor)(lintExecutorBackend(operation))
  }

  abstract class Implicits extends BackendImplicits
}