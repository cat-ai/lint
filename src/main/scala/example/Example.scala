package example


import java.util.concurrent.Executors

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

    def outPrint(s: String, t: Long): Unit = println("%-16s".format(s) + formatter.format(t) + s" $timeUnitStr")

    println(s"$name microbenchmark")

    var t0 = System.nanoTime()
    var result = block
    var t1 = System.nanoTime()

    outPrint("First Run", (t1 - t0) / divisor)

    val results =
      for (i <- 1 to iterations)
        yield {
          t0 = System.nanoTime()
          result = block
          t1 = System.nanoTime()
          outPrint("Run #" + i, (t1 - t0) / divisor)
          t1 - t0
        }

    println

    println(s"<++++++++++ $name results ++++++++++>")

    outPrint("Max:", results.max / divisor)
    outPrint("Min:", results.min / divisor)
    outPrint("Avg:", (results.sum / results.length) / divisor)
    outPrint(s"$name execution time: ",  results.sum / divisor)

    println
  }

  val lint = Lint()

  microbenchmark("Lint", "ms", 50) {
    for (i <- 0 to 1000000000)
      lint -~> { () => s"${math sqrt i}" }
  }

  lint stop()

  val executorService = Executors.newFixedThreadPool(sys.runtime.availableProcessors)

  microbenchmark("ExecutorService", "ms", 50) {
    for (i <- 0 to 500000) {
      executorService execute { () => s"${math sqrt i}" }
    }
  }

  executorService shutdownNow()
}
