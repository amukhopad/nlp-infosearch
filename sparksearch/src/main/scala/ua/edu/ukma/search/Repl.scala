package ua.edu.ukma.search

import java.time.LocalTime

import org.apache.spark.sql.expressions.UserDefinedFunction
import org.apache.spark.sql.{Dataset, Row, SparkSession}
import org.apache.spark.sql.functions._
import ua.edu.ukma.index.{TextProcessor, TimeUtil}

import scala.io.StdIn

object Repl {

  def main(args: Array[String]): Unit = {
    implicit val startTime: LocalTime = LocalTime.from(LocalTime.now())
    implicit val spark: SparkSession = SparkSession.builder()
      .appName("index")
      .master(s"local[*]")
      .getOrCreate()

    implicit val proc: TextProcessor = TextProcessor()

    val index = spark.read.parquet("index/reverse-tf-idf")



    TimeUtil.printFinishTime(startTime)

    repl(index)
  }

  def repl(index: Dataset[Row])(implicit proc: TextProcessor, spark: SparkSession): Unit = {
    val se = SearchEngine(proc)

    while (true) {
      print("Search > ")
      val query = StdIn.readLine()

      val result = se.search(query, index)
      result.foreach(title => println(s"-- ${title}"))
    }
  }

}
