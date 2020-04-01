package io.cat.ai.lint.concurrent.atomic

import java.util.concurrent.atomic.AtomicLong
import java.util.concurrent.locks.LockSupport

class FastAtomicLong(initValue: Long = 0) {

  private[this] val value: AtomicLong = new AtomicLong(initValue)

  def get: Long = value.get

  def incrementAndGet: Long = {
    var cond: Boolean = true
    var result: Long = 0

    while (cond) {
      val cur = get
      val next = cur + 1

      if (compareAndSet(cur, next)) {
        cond = false
        result = next
      }
    }
    result
  }

  def compareAndSet(cur: Long, next: Long): Boolean =
    if (value.compareAndSet(cur, next))
      true
    else {
      LockSupport parkNanos 1
      false
    }
}