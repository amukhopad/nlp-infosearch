package ua.edu.ukma.index

import com.johnsnowlabs.nlp.{DocumentAssembler, Finisher}
import com.johnsnowlabs.nlp.annotators.{Normalizer, Stemmer, Tokenizer}
import org.apache.spark.ml.param.ParamMap
import org.apache.spark.ml.{Estimator, Pipeline, PipelineModel}
import org.apache.spark.sql.{Dataset, SparkSession}
import org.apache.spark.sql.types.StructType

class TextProcessor(lang: String,
    inputCol: String, outputCol: String) extends Estimator[PipelineModel] {
  private val documentAssembler = new DocumentAssembler()
    .setInputCol(inputCol)
    .setOutputCol("document")
  private val regexTokenizer = new Tokenizer()
    .setInputCols(Array("document"))
    .setOutputCol("tokens")
  private val normalizer = new Normalizer()
    .setInputCols("tokens")
    .setOutputCol("normalized")
  private val stemmer = new Stemmer()
    .setInputCols("normalized")
    .setOutputCol("stemmed")
    .setLanguage(lang)
  private val finisher = new Finisher()
    .setInputCols("stemmed")
    .setOutputCols(outputCol)
  private val pipeline = new Pipeline()
    .setStages(Array(
      documentAssembler,
      regexTokenizer,
      normalizer,
      stemmer,
      finisher
    ))
  override val uid: String = pipeline.uid

  def setInputCol(col: String): TextProcessor = TextProcessor(lang, col, outputCol)

  def setOutputCol(col: String): TextProcessor = TextProcessor(lang, inputCol, col)

  override def fit(dataset: Dataset[_]): PipelineModel = pipeline.fit(dataset)

  override def copy(extra: ParamMap): Estimator[PipelineModel] = pipeline.copy(extra)

  override def transformSchema(schema: StructType): StructType = pipeline.transformSchema(schema)

  def transformQuery(query: String)(implicit spark: SparkSession): String = {
    import spark.implicits._

    val df = spark.createDataset(Seq(query)).toDF(inputCol)

    this.fit(df).transform(df)
      .select(outputCol)
      .map(_.getList[String](0).get(0))
      .first()
  }
}

object TextProcessor {
  def apply(lang: String = "english",
      inputCol: String = "_VALUE",
      outputCol: String = "terms"): TextProcessor = new TextProcessor(lang, inputCol, outputCol)
}
