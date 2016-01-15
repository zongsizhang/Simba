package org.apache.spark.examples.sql

import org.apache.spark.sql.{Point, SQLContext}
import org.apache.spark.{SparkContext, SparkConf}

import scala.collection.mutable.ListBuffer

/**
 * Created by gefei on 15-7-21.
 */
object DataFrameApiExample {
  case class PointData(name: String, x: Double, y: Double, z: Double)

  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf().setAppName("DataFrameApiExample").setMaster("local[2]")
    val sc = new SparkContext(sparkConf)
    val sqlContext = new SQLContext(sc)
    sqlContext.setConf("spark.sql.shuffle.partitions", 4.toString)
    sqlContext.setConf("spark.sql.sampleRate", 1.toString)

    import sqlContext.implicits._

    val point1 = sc.parallelize((1 to 1000).map(x => PointData(x.toString, x, x, x))).toDF()
    val point2 = sc.parallelize((1 to 1000).map(x => PointData((x + 1).toString, x, x, x))).toDF()

    point1.registerTempTable("point1")
    point2.registerTempTable("point2")

    val df1 = sqlContext.sql("SELECT * FROM point1")
    val df2 = sqlContext.sql("SELECT * FROM point2")

    df1.range(Point(point1("x"), point1("y"), point1("z")), Point(1.0, 1.0, 1.0),
      Point(5.0, 5.0, 5.0)).collect().foreach(println)
    df1.range(Array("x", "y", "z"), Array(1.0, 1.0, 1.0), Array(1.0, 1.0, 1.0)).collect().foreach(println)

    df1.circleRange(Array("x", "y"), Array(3.0, 3.0), 3).collect().foreach(println)
    df1.circleRange(Point(point1("x"), point1("y")), Point(3.0, 3.0), 1.6).collect().foreach(println)

    df1.range(Array("x", "y"), Array(1.0, 1.0), Array(3.0, 3.0)).collect().foreach(println)
    df1.range(Point(point1("x"), point1("y")), Point(1.0, 1.0), Point(3.0, 3.0)).collect().foreach(println)

    df1.knn(Array("x", "y"), Array(3.0, 3.0), 3).collect().foreach(println)
    df1.knn(Point(point1("x"), point1("y")), Point(3.0, 3.0), 3).collect().foreach(println)

    df1.distanceJoin(df2, Point(point1("x"), point1("y")),
      Point(point2("x"), point2("y")), 3).collect().foreach(println)
    df1.distanceJoin(df2, Array("x", "y"), Array("x", "y"), 3).collect().foreach(println)

    df1.knnJoin(df2, Point(point1("x"), point1("y")), Point(point2("x"), point2("y")), 3).collect().foreach(println)
    df1.knnJoin(df2, Array("x", "y"), Array("x", "y"), 3).collect().foreach(println)

    sc.stop()
    println("Finished.")
  }
}