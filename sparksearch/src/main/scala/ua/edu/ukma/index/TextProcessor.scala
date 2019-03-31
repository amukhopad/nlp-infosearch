package ua.edu.ukma.index

import com.johnsnowlabs.nlp.{DocumentAssembler, Finisher, TokenAssembler}
import com.johnsnowlabs.nlp.annotators.{Normalizer, Stemmer}
import org.apache.spark.ml.feature.RegexTokenizer
import org.apache.spark.ml.param.ParamMap
import org.apache.spark.ml.{Estimator, Pipeline, PipelineModel}
import org.apache.spark.sql.{Dataset, SparkSession}
import org.apache.spark.sql.types.StructType

class TextProcessor(lang: String,
    inputCol: String, outputCol: String) extends Estimator[PipelineModel] {

  private val regexTokenizer = new RegexTokenizer()
    .setInputCol(inputCol)
    .setOutputCol("tokens")
    .setPattern("\\w+")
    .setGaps(false)
  private val documentAssembler = new TokenAssembler()
    .setInputCols("tokens")
    .setOutputCol("document")
  private val normalizer = new Normalizer()
    .setInputCols("documents")
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
      regexTokenizer,
      documentAssembler,
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
