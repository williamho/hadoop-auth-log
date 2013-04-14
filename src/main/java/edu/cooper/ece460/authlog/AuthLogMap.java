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

/** Mapper takes lines of input and separates based on daemon type */
public class AuthLogMap extends Mapper<LongWritable, Text, Text, Text> {
	@Override
	public void map(LongWritable key, Text value, Context context) 
		throws IOException, InterruptedException
	{
		/* Line format (space-separated):
			Month Day Time Host Daemon[1234]: Daemon-specific-format

			Square brackets not always present.
		*/
		String line = value.toString();
		String[] parts = line.split("\\s+",6); // whitespace

		String daemon = parts[4];

		int splitPos = daemon.indexOf("[");
		if (splitPos >= 0)
			daemon = daemon.substring(0, splitPos);
		else	
			daemon = daemon.substring(0, daemon.length()-1); // Remove trailing :

		context.write(new Text(daemon), new Text(line));
	}
}

