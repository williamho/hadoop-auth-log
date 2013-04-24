package edu.cooper.ece460.authlog;

import java.io.*;
import java.util.*;

import org.apache.hadoop.fs.*;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.util.*;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.hbase.HBaseConfiguration;

public class HadoopAuthLog {
    public static void main (String[] args) throws Exception {
        Configuration conf = HBaseConfiguration.create();
        Path inPath = new Path(args[0]);
        Path outPath = new Path(args[1]);

        Job job1 = new Job(conf, "HadoopAuthLog");
        job1.setJarByClass(HadoopAuthLog.class);
        job1.setMapperClass(AuthLogMap.class);
        job1.setReducerClass(AuthLogReduce.class);
        job1.setNumReduceTasks(12);
        job1.setMapOutputKeyClass(Text.class);
        job1.setMapOutputValueClass(Text.class);

        TableMapReduceUtil.initTableReducerJob(
                "authlog",        // output table
                AuthLogReduce.class,    // reducer class
                job1
                );

        job1.setInputFormatClass(TextInputFormat.class);
        job1.setOutputFormatClass(TextOutputFormat.class);
        FileInputFormat.addInputPath(job1, inPath);
        FileOutputFormat.setOutputPath(job1, outPath);
        job1.waitForCompletion(true);
    }
}

