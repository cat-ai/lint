package io.cat.ai.lint

import io.cat.ai.lint.concurrent.lint.backend

package object concurrent {

  object Implicits extends backend.Implicits

  object alias {
    type Operation = Runnable
  }

  sealed trait ProducerConsumerPolicy

  case object SingleProducerSingleConsumer extends ProducerConsumerPolicy

  case object SingleProducerMultipleConsumer extends ProducerConsumerPolicy

  case object MultipleProducerSingleConsumer extends ProducerConsumerPolicy

  case object MultipleProducerMultipleConsumer extends ProducerConsumerPolicy
}