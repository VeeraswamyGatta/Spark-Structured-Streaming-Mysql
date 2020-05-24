package com.spark.struct
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.SaveMode


object SparkStructMysqlApp {

  def main(args: Array[String]) = {

    val spark = SparkSession
      .builder
      .appName("Mysql_read_and_write")
      .getOrCreate()

    println(" Arguments count  :::" + args.length)

    if (args.length == 3) {

      val hostname=args(0);
      val user = args(1);
      val password=args(2);

      val url = "jdbc:mysql://"+hostname+":3306/performance_schema?autoReconnect=true&useSSL=false";


      val driver="com.mysql.jdbc.Driver";

      val jdbcDF = spark.read
        .format("jdbc")
        .option("url", url)
        .option("driver", driver)
        .option("dbtable", "variables_by_thread")
        .option("user", user)
        .option("password", password)
        .load()

      jdbcDF.show(10,false)
      // Saving data to a JDBC source with Over write , if we want to append instead recreating ,need to update from Overwrite to Append
      jdbcDF.write
        .format("jdbc")
        .mode(SaveMode.Overwrite)
        .option("createTableColumnTypes", "THREAD_ID BIGINT, VARIABLE_NAME VARCHAR(64) ,VARIABLE_VALUE VARCHAR(1024) ")
        .option("url", url)
        .option("driver", driver)
        .option("dbtable", "mdfpoc.variables_by_thread_test")
        .option("user", user)
        .option("password", password)
        .save()

      println("Number of records are read from variables_by_thread" + jdbcDF.count())

    }else{
      println(" Please pass the mysql details (hostname,username,password) in the arguments")
      spark.stop()
    }
    spark.stop()
  }

}
