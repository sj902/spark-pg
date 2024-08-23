package sparkpkg

import org.apache.spark.SparkConf
import org.apache.spark.sql.functions._
import org.apache.spark.sql.SparkSession


object Main {
  def main(args: Array[String]): Unit = {
    println("Hello world!")

    val sparkConf = new SparkConf().setMaster("local").setAppName(s"SAS Monitor")

    val spark: SparkSession =
      SparkSession.builder
        .config(sparkConf)
        .getOrCreate()

    import spark.implicits._

    val counts = Seq(("abc", 1), ("def", 2), ("ghi", 3), ("abc", 5)).toDF("acc", "txn")

    counts.show()
    val map = counts
      .groupBy("acc")
      .agg(count("txn").as("count"))
      .sort($"count".desc)
      .limit(2)
      .collect()
      .map(row => (row(0), row(1)))
      .toMap
    println(map)
  }
}
