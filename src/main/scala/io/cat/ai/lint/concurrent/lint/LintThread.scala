package io.cat.ai.lint.concurrent.lint

import scala.language.postfixOps

/**
  * Smallest unit of the operation that has to be done the Backend
  **/
protected abstract class LintThread protected [lint] (operation: Runnable) extends Thread(operation) {

  override def toString: String = getName
}