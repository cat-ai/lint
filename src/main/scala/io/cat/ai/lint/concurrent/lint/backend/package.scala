package io.cat.ai.lint.concurrent.lint

package object backend {

  abstract class Implicits {

    private def defLintBackend: LintBackend = new DefaultLintBackend(FIFO, sys.runtime.availableProcessors)

    implicit val lintExecutorBackend: AbstractLintExecutorBackend = AbstractLintExecutorBackend()

    implicit val defaultLintBackend: LintBackend = defLintBackend
  }
}