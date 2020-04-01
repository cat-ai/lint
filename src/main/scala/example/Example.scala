package example


import java.util.concurrent.{Executor, Executors}

import io.cat.ai.lint.concurrent.Implicits._
import io.cat.ai.lint.concurrent.lint.Lint

object Example extends App {

  val formatter = java.text.NumberFormat.getIntegerInstance

  def microbenchmark[R](name: String, timeUnitStr: String = "ns", iterations: Int = 30)(block: => R): Unit = {

    val factor: Int = timeUnitStr match {
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

    outPrint("First Run", (t1 - t0) / factor)

    var lst = for (i <- 1 to iterations) yield {
      t0 = System.nanoTime()
      result = block
      t1 = System.nanoTime()
      outPrint("Run #" + i, (t1 - t0) / factor)
      t1 - t0
    }

    println

    println(s"<++++++++++ $name results ++++++++++>")

    outPrint("Max:", lst.max / factor)
    outPrint("Min:", lst.min / factor)
    outPrint("Avg:", (lst.sum / lst.length) / factor)
    outPrint(s"$name execution time: ",  lst.sum / factor)
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
