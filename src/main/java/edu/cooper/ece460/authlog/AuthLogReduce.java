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

/** AuthLogReduce checks the key (daemon) and calls the appropriate 
	daemon-specific reduce function */
public class AuthLogReduce extends Reducer<Text, Text, Text, Text> {
	@Override
	public void reduce(Text key, Iterable<Text> values, Context context) 
		throws IOException, InterruptedException 
	{
		int ssh_count = 0, login_count = 0, failed_count = 0;
		for (Text value : values) {
			String stringValue = value.toString();

			if (stringValue.equals("ssh"))
				ssh_count++;
			else if (stringValue.equals("login"))
				login_count++;
			else if (stringValue.equals("failed"))
				failed_count++;
		}

		String outputKey = login_count-ssh_count + "," + ssh_count + "," + failed_count;
		context.write(key, new Text(outputKey));
	}
}

