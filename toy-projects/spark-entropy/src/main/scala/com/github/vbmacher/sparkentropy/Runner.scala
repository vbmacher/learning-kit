package com.github.vbmacher.sparkentropy

import org.apache.spark.sql.functions._
import org.apache.spark.sql.{DataFrame, SparkSession}

object Runner extends App {

  implicit val spark = SparkSession.builder()
    .master("local")
    .getOrCreate()

  import spark.implicits._


  def entropy(df: DataFrame, what: String, totalCount: Long) = {
    df
      .groupBy(col(what)).count()
      .withColumn("value", 'count / totalCount) // probability
      .withColumn("value", when('value === 0, 0.0).otherwise(-'value * log('value))) // partial entropy
      .select('value).as[Double]
      .reduce((v1, v2) => v1 + v2) // result
  }

  val path = getClass.getResource("/lastfm_subset").getPath
  val data = spark.read.json(path)
  val totalCount = data.count()

  val expectedEntropy = math.log(totalCount)
  val artistsEntropy = entropy(data, "artist", totalCount)
  val songsEntropy = entropy(data, "title", totalCount)

  println(s"Total rows: $totalCount")
  println(s"Expected entropy: $expectedEntropy")
  println(s"Artists entropy: $artistsEntropy")
  println(s"Songs entropy: $songsEntropy")

  data
    .groupBy('artist)
    .agg(count('title) as "count", collect_list('title) as "tracks")
    .sort('count.desc)
    .show()

  data
    .groupBy('title)
    .count()
    .sort('count.desc)
    .show()

  spark.stop()
}
