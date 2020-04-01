package io.cat.ai.lint.control


/**
  * Represents an object that you can wait for
  * */
trait Waitable {

  /**
    * Waits until there are no active operations
    **/
  def waitForNew(): Unit

  /**
    * Waits while all active operations are completed
    **/
  def waitToComplete(): Unit
}