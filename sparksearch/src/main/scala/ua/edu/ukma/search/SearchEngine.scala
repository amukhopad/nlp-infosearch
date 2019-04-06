package ua.edu.ukma.search

import java.time.LocalTime

import org.apache.spark.sql.functions._
import org.apache.spark.sql.{Dataset, Row, SparkSession}
import ua.edu.ukma.index.{TextProcessor, TimeUtil}

class SearchEngine(proc: TextProcessor)(implicit spark: SparkSession) {

  import spark.implicits._

  def search(query: String, index: Dataset[Row], maxResults: Int = 20): Array[String] = {
    implicit val startTime = LocalTime.from(LocalTime.now())

    val termFilter = query.split("\\s+")
      .map(proc.transformQuery)
      .map(term => s"terms = '${term}'")
      .mkString(" OR ")

    val search = index.filter(termFilter)
      .groupBy('title)
      .agg(sum('tf_idf) as 'tfidf)
      .orderBy(desc("tfidf"))
      .select('title)
      .cache()

    val result = search
      .map(_.getString(0))
      .take(maxResults)

    val total = search.count()

    val (_, m, s) = TimeUtil.getFinishTime
    println(s"Showing top ${result.length} results across ${total} total in $m m $s s")

    result
  }

}

object SearchEngine {
  def apply(proc: TextProcessor)(implicit spark: SparkSession): SearchEngine =
    new SearchEngine(proc)
}
