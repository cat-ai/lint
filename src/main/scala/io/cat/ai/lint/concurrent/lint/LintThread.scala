package io.cat.ai.lint.concurrent.lint

import io.cat.ai.lint.concurrent.alias.Operation

import scala.language.postfixOps

/**
  * Smallest unit of the operation that has to be done the Backend
  **/
protected abstract class LintThread protected [lint] (operation: Operation) extends Thread(operation) {

  override def toString: String = getName
}