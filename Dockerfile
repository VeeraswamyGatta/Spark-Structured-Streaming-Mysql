FROM apache/spark:latest
COPY build/libs/spark-structured-mysql-gradle-1.0-SNAPSHOT-shadedjar.jar /opt/spark/jars
RUN sh -c 'touch /opt/spark/jars/spark-structured-mysql-gradle-1.0-SNAPSHOT-shadedjar.jar'
