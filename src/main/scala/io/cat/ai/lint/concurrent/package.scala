package io.cat.ai.lint

import io.cat.ai.lint.concurrent.lint.backend

package object concurrent {

  object Implicits extends backend.Implicits

  object alias {
    type Operation = Runnable
  }

  // Categorization: producer/consumer policy
  sealed trait ProducerConsumerPolicy

  case object SingleProducerSingleConsumer     extends ProducerConsumerPolicy
  case object SingleProducerMultipleConsumer   extends ProducerConsumerPolicy
  case object MultipleProducerSingleConsumer   extends ProducerConsumerPolicy
  case object MultipleProducerMultipleConsumer extends ProducerConsumerPolicy

  sealed trait Mode

  case object FIFO extends Mode
  case object LIFO extends Mode
}