package ua.edu.ukma.search

import java.time.LocalTime

import org.apache.spark.sql.{Dataset, Row, SparkSession}
import ua.edu.ukma.index.{TextProcessor, TimeUtil}

import scala.io.StdIn

object Search {

  def main(args: Array[String]): Unit = {
    implicit val startTime: LocalTime = LocalTime.from(LocalTime.now())
    implicit val spark: SparkSession = SparkSession.builder()
      .appName("index")
      .master(s"local[*]")
      .getOrCreate()

    implicit val proc: TextProcessor = TextProcessor()

    val index = spark.read.parquet("reverse_index")

    TimeUtil.printFinishTime(startTime)

    repl(index)
  }

  def repl(index: Dataset[Row])(implicit proc: TextProcessor, spark: SparkSession): Unit = {
    var line = ""
    while (true) {
      print("Search > ")
      val query = StdIn.readLine()

      val result = searchWord(query, index)

      result.foreach(title => println(s"-- ${title}"))
    }
  }

  def searchWord(query: String, index: Dataset[Row])
        (implicit proc: TextProcessor, spark: SparkSession): Array[String] = {
    implicit val startTime = LocalTime.from(LocalTime.now())
    import spark.implicits._

    val tranformed = proc.transformQuery(query)

    val result = index.filter(s"terms = '${tranformed}'")
      .select("title")
      .map(_.getString(0)).collect()

    val (_, m, s) = TimeUtil.getFinishTime
    println(s"Found ${result.length} results in $m m $s s")

    result
  }
}
