package io.cat.ai.lint.concurrent

import java.util.concurrent.atomic.{AtomicBoolean, AtomicInteger}

import io.cat.ai.lint.concurrent.atomic.AtomicBooleanOps._

import scala.language.postfixOps

package object atomic {

  object Implicits extends AtomicBooleanImplicits with AtomicIntImplicits

  abstract class AtomicBooleanImplicits {

    def is(atomicBoolean: AtomicBoolean)(implicit ops: AtomicBooleanOp): Boolean = ops getValue atomicBoolean

    def not(atomicBoolean: AtomicBoolean)(implicit ops: AtomicBooleanOp): Boolean = !is(atomicBoolean)(ops)

    implicit class AtomicBooleanNewValAssigner(atomicBoolean: AtomicBoolean) {
      def :=(newValue: Boolean): Unit = atomicBoolean.set(newValue)
    }
  }

  trait AtomicIntImplicits {

    implicit class AtomicIntegerIntTC(atomicInteger: AtomicInteger) {

      @inline private def get: Int = atomicInteger.get

      def <<(x: Int): Int = get << x
      def <<(x: Long): Int = get << x

      def >>>(x: Int): Int = get >>> x
      def >>>(x: Long): Int = get >>> x

      def >>(x: Int): Int = get >> x
      def >>(x: Long): Int = get >> x

      def =!=(x: Byte): Boolean = get != x
      def =!=(x: Short): Boolean = get != x
      def =!=(x: Char): Boolean = get != x
      def =!=(x: Int): Boolean = get != x
      def =!=(x: Long): Boolean = get != x
      def =!=(x: Float): Boolean = get != x
      def =!=(x: Double): Boolean = get != x

      def <(x: Byte): Boolean = get < x
      def <(x: Short): Boolean = get < x
      def <(x: Char): Boolean = get < x
      def <(x: Int): Boolean = get < x
      def <(x: Long): Boolean = get < x
      def <(x: Float): Boolean = get < x
      def <(x: Double): Boolean = get < x

      def <=(x: Byte): Boolean = get <= x
      def <=(x: Short): Boolean = get <= x
      def <=(x: Char): Boolean = get <= x
      def <=(x: Int): Boolean = get <= x
      def <=(x: Long): Boolean = get <= x
      def <=(x: Float): Boolean = get <= x
      def <=(x: Double): Boolean = get <= x

      def >(x: Byte): Boolean = get > x
      def >(x: Short): Boolean = get > x
      def >(x: Char): Boolean = get > x
      def >(x: Int): Boolean = get > x
      def >(x: Long): Boolean = get > x
      def >(x: Float): Boolean = get > x
      def >(x: Double): Boolean = get > x

      def >=(x: Byte): Boolean = get >= x
      def >=(x: Short): Boolean = get >= x
      def >=(x: Char): Boolean = get >= x
      def >=(x: Int): Boolean = get >= x
      def >=(x: Long): Boolean = get >= x
      def >=(x: Float): Boolean = get >= x
      def >=(x: Double): Boolean = get >= x

      def |(x: Byte): Int = get | x
      def |(x: Short): Int = get | x
      def |(x: Char): Int = get | x
      def |(x: Int): Int = get | x
      def |(x: Long): Long = get | x

      def &(x: Byte): Int = get & x
      def &(x: Short): Int = get & x
      def &(x: Char): Int = get & x
      def &(x: Int): Int = get & x
      def &(x: Long): Long = get & x

      def ^(x: Byte): Int = get ^ x
      def ^(x: Short): Int = get ^ x
      def ^(x: Char): Int = get ^ x
      def ^(x: Int): Int = get ^ x
      def ^(x: Long): Long = get ^ x

      def +(x: Byte): Int = atomicInteger updateAndGet(i => i + x)
      def +(x: Short): Int = atomicInteger updateAndGet(i => i + x)
      def +(x: Char): Int = atomicInteger updateAndGet(i => i + x)
      def +(x: Int): Int = atomicInteger updateAndGet(i => i + x)
      def +(x: Long): Int = atomicInteger updateAndGet(i => i + x toInt)
      def +(x: Float): Int = atomicInteger updateAndGet(i => i + x toInt)
      def +(x: Double): Int = atomicInteger updateAndGet(i => i + x toInt)

      def -(x: Byte): Int = atomicInteger updateAndGet(i => i - x)
      def -(x: Short): Int = atomicInteger updateAndGet(i => i - x)
      def -(x: Char): Int = atomicInteger updateAndGet(i => i - x)
      def -(x: Int): Int = atomicInteger updateAndGet(i => i - x)
      def -(x: Long): Int = atomicInteger updateAndGet(i => i - x toInt)
      def -(x: Float): Int = atomicInteger updateAndGet(i => i - x toInt)
      def -(x: Double): Int = atomicInteger updateAndGet(i => i - x toInt)

      def *(x: Byte): Int = atomicInteger updateAndGet(i => i * x)
      def *(x: Short): Int = atomicInteger updateAndGet(i => i * x)
      def *(x: Char): Int = atomicInteger updateAndGet(i => i * x)
      def *(x: Int): Int = atomicInteger updateAndGet(i => i * x)
      def *(x: Long): Int = atomicInteger updateAndGet(i => i * x toInt)
      def *(x: Float): Int = atomicInteger updateAndGet(i => i * x toInt)
      def *(x: Double): Int = atomicInteger updateAndGet(i => i * x toInt)

      def /(x: Byte): Int = atomicInteger updateAndGet(i => i / x)
      def /(x: Short): Int = atomicInteger updateAndGet(i => i / x)
      def /(x: Char): Int = atomicInteger updateAndGet(i => i / x)
      def /(x: Int): Int = atomicInteger updateAndGet(i => i / x)
      def /(x: Long): Int = atomicInteger updateAndGet(i => i / x toInt)
      def /(x: Float): Int = atomicInteger updateAndGet(i => i / x toInt)
      def /(x: Double): Int = atomicInteger updateAndGet(i => i / x toInt)

      def %(x: Byte): Int = atomicInteger updateAndGet(i => i % x)
      def %(x: Short): Int = atomicInteger updateAndGet(i => i % x)
      def %(x: Char): Int = atomicInteger updateAndGet(i => i % x)
      def %(x: Int): Int = atomicInteger updateAndGet(i => i % x)
      def %(x: Long): Int = atomicInteger updateAndGet(i => i % x toInt)
      def %(x: Float): Int = atomicInteger updateAndGet(i => i % x toInt)
      def %(x: Double): Int = atomicInteger updateAndGet(i => i % x toInt)

      def ===(x: Byte): Boolean = get == x
      def ===(x: Short): Boolean = get == x
      def ===(x: Char): Boolean = get == x
      def ===(x: Int): Boolean = get == x
      def ===(x: Long): Boolean = get == x
      def ===(x: Float): Boolean = get == x
      def ===(x: Double): Boolean = get == x
    }
  }
}