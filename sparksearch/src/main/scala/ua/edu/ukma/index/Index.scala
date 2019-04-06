package ua.edu.ukma.index

import java.time._

import com.databricks.spark.xml._
import org.apache.spark.sql.expressions.UserDefinedFunction
import org.apache.spark.sql.functions._
import org.apache.spark.sql.{SaveMode, SparkSession}
import ua.edu.ukma.search.SearchEngine


object Index extends App {

  val cores = 4
  val partitions = 4 * cores

  val spark = SparkSession.builder()
    .appName("index")
    .master("local[4]")
    .config("spark.sql.shuffle.partitions", "16")
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
  //.checkpoint()


  val index = tokenized
    .select(explode('term_array) as "terms", 'title).distinct
    .filter('terms.isNotNull && 'title.isNotNull)
    .filter(_.getString(0).length() < 200)

  val total = tokenized
    .select('term_array, explode('term_array) as "terms", 'title)
    .filter('terms.isNotNull && 'title.isNotNull)
    .withColumn("words_total", wc('term_array))

  val n = total.select(count('term_array)).first().getLong(0)

  val tf = total
    .groupBy('title, 'words_total, 'terms)
    .agg(count('term_array) as "tc")
    .select('title, 'terms, ('tc / 'words_total) as "tf")

  val withDf = index.groupBy("terms")
    .agg(countDistinct('title) as "df")

  val withIdf = withDf.withColumn("idf", idf(n)('df))

  val tfIdf = tf
    .join(withIdf, Seq("terms"), "left")
    .withColumn("tf_idf", 'tf * 'idf)


  tfIdf.repartition('terms).write
    .mode(SaveMode.Overwrite)
    .parquet("file:/Users/enginebreaksdown/dev/spark-search/index/reverse-tf-idf")

  TimeUtil.printFinishTime
}
