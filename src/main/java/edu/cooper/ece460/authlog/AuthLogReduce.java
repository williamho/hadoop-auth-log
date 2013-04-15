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
		String output;
		int count = 0;
		for (Text value : values) {
			count += 1;
		}
		context.write(key, new Text(Integer.toString(count)));
	}

	/** Do something with the line based on the daemon type */
	// private void daemonReduce(Text key, Text value, Context context) 
		// throws IOException, InterruptedException 
	// {
		// String daemon = key.toString().toLowerCase();
		// String line = value.toString();

		// if (daemon.equals("login")) {
			// context.write(key, new Text(line)); // placeholder
		// }
		// else if (daemon.equals("crond")) {
			// // Do something with line (e.g., call a function)
		// }
		// else if (daemon.equals("whatever else")) {
			// // etc
		// }
	// }

}

