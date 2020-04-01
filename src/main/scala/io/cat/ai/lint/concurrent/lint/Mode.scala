package io.cat.ai.lint.concurrent.lint

sealed trait Mode

case object FIFO extends Mode

case object LIFO extends Mode