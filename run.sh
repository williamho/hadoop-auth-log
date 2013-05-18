#!/bin/bash

set -e

IN_PATH=${1:-authlog_in}
OUT_PATH=${2:-authlog_out}
OUT_PATH_LOCAL=${3:-output}

rm ${OUT_PATH_LOCAL}/$OUT_PATH || true
hadoop fs -rmr $OUT_PATH || true
hadoop jar target/HadoopAuthLog-1.0.jar edu.cooper.ece460.authlog.HadoopAuthLog $IN_PATH $OUT_PATH
hadoop fs -getmerge $OUT_PATH ${OUT_PATH_LOCAL}/$OUT_PATH
./postprocess.py $OUT_PATH_LOCAL >authlog_out
hadoop fs -mkdir testdata
hadoop fs -rmr testdata/authlog_out
hadoop fs -put authlog_out testdata
$MAHOUT_HOME/bin/mahout org.apache.mahout.clustering.syntheticcontrol.kmeans.Job
hadoop fs -get output $OUT_PATH
$MAHOUT_HOME/bin/mahout clusterdump -i output/clusters-*-final -o out.txt --pointsDir output/clusteredPoints
