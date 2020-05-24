# Spark-Structured-Streaming-Mysql


//Connect to MYsql , kubectl get pods -- 

kubectl exec -it mysql-596c8cc479-mc7p9 bash

mysql -h mysql.default.svc.cluster.local -P3306 -u root -5LX1gtXeeD


// Connect to Spark pod to debug or submit your jobs

kubectl exec -it spark-sparkoperator-6f4b6fcd8-7z8xc -n spark-operator bash

// Submit application : Please check once app name and driver name if exists , please change before submit otherwise app already exists will come

/opt/spark/bin/spark-submit --class com.spark.struct.SparkStructMysqlApp --master k8s://https://172.21.0.1:443 --deploy-mode cluster --conf spark.kubernetes.namespace=spark-apps --conf spark.app.name=spark-mysql-testing --conf spark.kubernetes.driver.pod.name=spark-mysql-testing-driver --conf spark.kubernetes.container.image=us.icr.io/mdfpoc/spark-structured-mysql-gradle:1.0.0 --conf spark.kubernetes.container.image.pullPolicy=Always --conf spark.kubernetes.submission.waitAppCompletion=false --conf spark.kubernetes.driver.label.sparkoperator.k8s.io/app-name=spark-mysql-testing --conf spark.kubernetes.driver.label.sparkoperator.k8s.io/launched-by-spark-operator=true  --conf spark.driver.cores=1 --conf spark.kubernetes.driver.limit.cores=1200m --conf spark.driver.memory=512m --conf spark.kubernetes.authenticate.driver.serviceAccountName=spark --conf spark.kubernetes.driver.label.version=2.4.5 --conf spark.kubernetes.executor.label.sparkoperator.k8s.io/app-name=spark-mysql-testing --conf spark.kubernetes.executor.label.sparkoperator.k8s.io/launched-by-spark-operator=true  --conf spark.executor.instances=1 --conf spark.executor.cores=1 --conf spark.executor.memory=512m --conf spark.kubernetes.executor.label.version=2.4.5 local:///opt/spark/mdf/jars/spark-structured-mysql-gradle-1.0-SNAPSHOT-shadedjar.jar mysql.default.svc.cluster.local root 5LX1gtXeeD


// Debug application

/opt/spark/bin/spark-shell --jars /opt/spark/mysql-connector-java-8.0.11.jar


import org.apache.spark.sql.SaveMode

val hostname="mysql.default.svc.cluster.local"
val user = "root";
val password="5LX1gtXeeD";

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
// Saving data to a JDBC source
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

val jdbcDF = spark.read
.format("jdbc")
.option("url", url)
.option("driver", driver)
.option("dbtable", "mdfpoc.variables_by_thread_test")
.option("user", user)
.option("password", password)
.load()

jdbcDF.count
