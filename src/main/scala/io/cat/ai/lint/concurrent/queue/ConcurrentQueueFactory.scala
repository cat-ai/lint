package io.cat.ai.lint.concurrent.queue

import java.util

import io.cat.ai.lint.concurrent._

import org.jctools.queues._

object ConcurrentQueueFactory {

  def apply[A](policy: ProducerConsumerPolicy, capacity: Int): util.Queue[A] = policy match {
    case SingleProducerSingleConsumer     => new SpscArrayQueue[A](capacity)
    case SingleProducerMultipleConsumer   => new SpmcArrayQueue[A](capacity)
    case MultipleProducerSingleConsumer   => new MpscArrayQueue[A](capacity)
    case MultipleProducerMultipleConsumer => new MpmcArrayQueue[A](capacity)
  }
}