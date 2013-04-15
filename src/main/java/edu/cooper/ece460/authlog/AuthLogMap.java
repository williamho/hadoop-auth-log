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
	Context context;

	@Override
	public void map(LongWritable key, Text value, Context context) 
		throws IOException, InterruptedException
	{
		this.context = context;
		/* Line format (space-separated):
		    Month Day Time Host Daemon[1234]: Daemon-specific-format
		   (Square brackets not always present) */
		String line = value.toString();
		String[] parts = line.split("\\s+"); // whitespace

		String host = parts[3];
		String daemon = parts[4];

		int splitPos = daemon.indexOf("[");
		if (splitPos >= 0)
			daemon = daemon.substring(0, splitPos);
		else	
			daemon = daemon.substring(0, daemon.length()-1); // Remove trailing :

		if (daemon.equals("systemd-logind") &&
		     parts[5].equals("New") && parts[6].equals("session")) {
			// Format: New session 6 of user username.
			String user = parts[10];
			user = user.substring(0, user.length()-1); // Remove trailing .
			writeOutput(user,host,"login");
		}
		else if (daemon.equals("sshd") && parts[5].equals("Accepted")) {
			// Format: Accepted password for username from 8.8.8.8 port 12345 ssh2
			String user = parts[8];
			writeOutput(user,host,"ssh");
		}
		else if (daemon.equals("login") && parts[5].equals("FAILED")) {
			// Format: FAILED LOGIN 1 FROM tty2 FOR username, Authentication failure
			String user = parts[11];
			user = user.substring(0, user.length()-1); // Remove trailing ,
			writeOutput(user,host,"failed");
		}	
	}

	private void writeOutput(String user, String host, String value) 
		throws IOException, InterruptedException
	{
		String outputKey = user + "," + host;
		context.write(new Text(outputKey), new Text(value));
	}
}

