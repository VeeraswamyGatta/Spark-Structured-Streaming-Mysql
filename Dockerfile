FROM apache/spark:latest
RUN mkdir -p /opt/spark/mdf/jars
COPY build/libs/spark-structured-mysql-gradle-1.0-SNAPSHOT-shadedjar.jar /opt/spark/mdf/jars
RUN sh -c 'touch /opt/spark/mdf/jars/spark-structured-mysql-gradle-1.0-SNAPSHOT-shadedjar.jar'
