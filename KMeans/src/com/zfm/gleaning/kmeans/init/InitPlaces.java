package com.zfm.gleaning.kmeans.init;

import java.io.IOException;
import java.net.URI;
import java.util.Random;
import java.util.List;
import java.util.ArrayList;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import org.apache.hadoop.io.Text;

import com.zfm.gleaning.kmeans.pojo.LngAndLatEntity;

public class InitPlaces {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws IllegalArgumentException 
	 * @throws InterruptedException 
	 * @throws ClassNotFoundException 
	 */
	public static void initPlaces(int k) throws IllegalArgumentException, IOException, ClassNotFoundException, InterruptedException {
		// TODO Auto-generated method stub
		
		Job job = new Job();
		job.setJarByClass(InitPlaces.class);
		job.setJobName("initPlaces");
		
		FileInputFormat.addInputPath(job, new Path("input/lal.txt"));
		
		
		String outputPath = "output/lal/center";
		
		Configuration conf = new Configuration();
		FileSystem fs = null;
		try {
			fs = FileSystem.get(URI.create(outputPath), conf);
			if(fs.exists(new Path(outputPath))){
				fs.delete(new Path(outputPath), true);
			}
		} catch (IllegalArgumentException | IOException e) {
			e.printStackTrace();
		}
		try {
			fs.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		FileOutputFormat.setOutputPath(job, new Path(outputPath));
		
		Configuration conf2 = job.getConfiguration();
		conf2.set("k", k+"");
		job.setMapperClass(InitPlacesMapper.class);
//		job.setCombinerClass(TopPlacesReducer.class);	//combiner:dont change the result
		job.setReducerClass(InitPlacesReducer.class);
		
		job.setMapOutputKeyClass(IntWritable.class);
		job.setMapOutputValueClass(LngAndLatEntity.class);
		
		job.setOutputValueClass(Text.class);
		job.setOutputKeyClass(Text.class);
		
		job.waitForCompletion(true);
	}

}
