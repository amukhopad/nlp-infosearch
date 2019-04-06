package ua.edu.ukma

import org.apache.spark.sql.expressions.UserDefinedFunction
import org.apache.spark.sql.functions.udf

package object index {
  def wc: UserDefinedFunction =
    udf((array: collection.mutable.WrappedArray[String]) => array.count(_ != null))

  def idf(n: Long): UserDefinedFunction =
    udf { df: Long => math.log((n + 1.0) / (df + 1)) }
}
