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

import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.hbase.mapreduce.TableReducer;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;

/** AuthLogReduce checks the key (daemon) and calls the appropriate
  daemon-specific reduce function */
public class AuthLogReduce extends TableReducer<Text, Text, ImmutableBytesWritable> {
    public static final byte[] cf = "userhost".getBytes();

    @Override
    public void reduce(Text key, Iterable<Text> values, Context context)
    throws IOException, InterruptedException
{
    String[] parts = key.toString().split(",");
    String user = parts[0];
    String host = parts[1];

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
    int local_login_count = login_count - ssh_count;

    Put p = new Put(Bytes.toBytes(key.toString()));
    p.add(cf, "user".getBytes(), Bytes.toBytes(user));
    p.add(cf, "host".getBytes(), Bytes.toBytes(host));
    p.add(cf, "local".getBytes(), Bytes.toBytes(local_login_count));
    p.add(cf, "ssh".getBytes(), Bytes.toBytes(ssh_count));
    p.add(cf, "failed".getBytes(), Bytes.toBytes(failed_count));

    context.write(null, p);
}
}

