package com.zfm.hadoop.mapper.gleaning.top;

import java.io.IOException;
import java.net.URI;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import org.apache.hadoop.io.Text;

import com.zfm.hadoop.mapper.gleaning.top.utils.WordSequmenter;

public class TopPlaces {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws IllegalArgumentException 
	 * @throws InterruptedException 
	 * @throws ClassNotFoundException 
	 */
	public static void topPlaces() throws IllegalArgumentException, IOException, ClassNotFoundException, InterruptedException {
		Job job = new Job();
		job.setJarByClass(TopPlaces.class);
		job.setJobName("topPlaces");
		
		FileInputFormat.addInputPath(job, new Path("input/places.txt"));
		
		String outputPath = "output/result";
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
		
		job.setMapperClass(TopPlacesMapper.class);
//		job.setCombinerClass(TopPlacesReducer.class);	//combiner:dont change the result
		job.setReducerClass(TopPlacesReducer.class);
		
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);
		
		job.waitForCompletion(true);
		/*System.exit(job.waitForCompletion(true) ? 0 : 1);*/
	}

}
