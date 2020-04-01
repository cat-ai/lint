package example


import java.util.concurrent.{Executor, Executors}

import io.cat.ai.lint.concurrent.Implicits._
import io.cat.ai.lint.concurrent.lint.Lint

object Example extends App {

  val formatter = java.text.NumberFormat.getIntegerInstance

  def microbenchmark[R](name: String, timeUnitStr: String = "ns", iterations: Int = 30)(block: => R): Unit = {

    val divisor: Int = timeUnitStr match {
      case "ms"  => 1000000
      case "sec" => 1000000000
      case _     => 1
    }

    def outPrint(s: String, t: Long): Unit = {
      println("%-16s".format(s) + formatter.format(t) + s" $timeUnitStr")
    }

    println(s"$name microbenchmark")

    var t0 = System.nanoTime()
    var result = block
    var t1 = System.nanoTime()

    outPrint("First Run", (t1 - t0) / divisor)

    var lst = for (i <- 1 to iterations) yield {
      t0 = System.nanoTime()
      result = block
      t1 = System.nanoTime()
      outPrint("Run #" + i, (t1 - t0) / divisor)
      t1 - t0
    }

    println

    println(s"<++++++++++ $name results ++++++++++>")

    outPrint("Max:", lst.max / divisor)
    outPrint("Min:", lst.min / divisor)
    outPrint("Avg:", (lst.sum / lst.length) / divisor)
    outPrint(s"$name execution time: ",  lst.sum / divisor)
    println
  }

  val lint = Lint()

  val range = 0 to 10000000

  microbenchmark("Lint", "ms", 50) {
    for (i <- range)
      lint -~> { () => s"${math sqrt i}" } -~> { () => s"${ math cbrt i}" }
  }

  lint stop()

  val executorService = Executors.newFixedThreadPool(sys.runtime.availableProcessors())

  microbenchmark("ExecutorService", "ms", 10) {
    for (i <- range) {
      executorService execute { () => s"${math sqrt i}" }
    }
  }

  executorService shutdownNow()
}
