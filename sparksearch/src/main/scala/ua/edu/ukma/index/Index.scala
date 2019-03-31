package ua.edu.ukma.index

import java.time._

import com.databricks.spark.xml._
import org.apache.spark.sql.{Row, SaveMode, SparkSession}
import org.apache.spark.sql.functions._


object Index extends App {

  val cores = 4
  val partitions = 4 * cores

  val spark = SparkSession.builder()
    .appName("index")
    .config("spark.sql.warehouse", "file:/Volumes/lacie-files/spark-warehouse")
    .config("spark.sql.shuffle.partitions", s"${partitions}")
    .getOrCreate()

 // spark.sparkContext.setCheckpointDir("file:/Volumes/lacie-files/checkpoint")

  import spark.implicits._

  implicit val startTime: LocalTime = LocalTime.from(LocalTime.now())

  val xmls = spark.read
    .option("rootTag", "mediawiki")
    .option("rowTag", "page")
    .option("excludeAttributes", "true")
    .xml("/Users/enginebreaksdown/Downloads/enwiki-20190320-pages-articles-multistream3.xml-p88445p200507")

  val dataset = xmls
    .filter('redirect.isNull)
    .select('title, explode(array('revision)).as("rev"))
    .select("title", "rev.text._VALUE")
    .where('_VALUE.isNotNull)

  val tokenized = TextProcessor()
    .setInputCol("_VALUE")
    .setOutputCol("term_array")
    .fit(dataset).transform(dataset)
   // .checkpoint()

  val map = tokenized
    .select(explode('term_array).as("terms"), 'title).distinct
    .filter('terms.isNotNull && 'title.isNotNull)
    .filter(_.getString(0).length() < 200)

  map.repartition('terms).write
    .mode(SaveMode.Overwrite)
    .parquet("file:///Users/enginebreaksdown/dev/spark-search/reverse_index")

  TimeUtil.printFinishTime
}
