#!/bin/bash

set -e

IN_PATH=${1:-authlog_in}
OUT_PATH=${2:-authlog_out}
OUT_PATH_LOCAL=${3:-output}

hadoop fs -rmr $OUT_PATH || true
hadoop jar target/HadoopAuthLog-1.0.jar edu.cooper.ece460.authlog.HadoopAuthLog $IN_PATH $OUT_PATH
hadoop fs -getmerge $OUT_PATH ${OUT_PATH_LOCAL}/$OUT_PATH

