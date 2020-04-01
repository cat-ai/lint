package io.cat.ai.lint.concurrent.atomic

import java.util.concurrent.atomic.AtomicBoolean

import scala.language.postfixOps

object AtomicBooleanOps {

  trait AtomicBooleanOp {
    def getValue(atomicBoolean: AtomicBoolean): Boolean
  }

  implicit val atomicBooleanOps: AtomicBooleanOp = _ get
}