javac -cp ".:$HADOOP/hadoop-core-1.1.1.jar:$HADOOP/lib/*" *.java -d target/
jar cvf build/K$1.jar -C target/ .
#hadoop jar build/K$1.jar org.ethz.las.PSGD /user/vincent/input /user/vincent/output/$2
#hadoop fs -getmerge /user/vincent/output/$2 model/$2.model