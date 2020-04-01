package io.cat.ai.lint.control

/**
  * Stopper trait whose instances can accept a stop request and indicate whether a stop has already been requested.
  * */
trait Stoppable {

  def stop(): Unit

  def isStopped: Boolean
}