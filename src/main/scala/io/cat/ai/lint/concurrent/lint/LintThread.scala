package io.cat.ai.lint.concurrent.lint

import io.cat.ai.lint.concurrent.alias.Operation

import scala.language.postfixOps

/**
  * Smallest unit of the operation in the execution that has to be submitted the executor backend [[io.cat.ai.lint.concurrent.lint.backend.LintExecutorBackend]]
  *
  * Each LintThread is executed in a [[io.cat.ai.lint.concurrent.lint.backend.LintExecutorBackend]]
  **/
protected abstract class LintThread protected [lint] (operation: Operation) extends Thread(operation) {

  override def toString: String = getName
}