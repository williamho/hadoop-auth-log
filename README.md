Analyzing auth.log with Hadoop
==============================

Process `auth.log` files (presumably pulled from multiple nodes with Flume) 
using Hadoop to output relevant statistics.

##Build

	mvn package

##Run

###Command-line

Run the jar file with Hadoop. Program arguments:

1. Path to the HDFS directory with input file(s)
2. Path to the HDFS directory to save output files (must not already exist)

Example:

	hadoop fs -rmr authlog_out
	hadoop jar target/HadoopAuthLog-1.0.jar edu.cooper.ece460.authlog.HadoopAuthLog authlog_in authlog_out
	hadoop fs -getmerge authlog_out output/authlog_out

###Shell script

The shell script `run.sh` in the root directory can be used to perform the
above steps. The HDFS output directory is automatically deleted by the script 
before starting the Hadoop job.

	./run.sh [hdfs-input-dir] [hdfs-output-dir] [local-output-dir]

Default values are `authlog_in`, `authlog_out`, and `output`, respectively.

###HTTP

To be added.

