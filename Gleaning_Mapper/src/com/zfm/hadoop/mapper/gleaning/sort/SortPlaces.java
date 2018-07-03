package com.zfm.hadoop.mapper.gleaning.sort;

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

import com.zfm.hadoop.mapper.gleaning.sort.pojo.KVEntity;
import com.zfm.hadoop.mapper.gleaning.top.utils.WordSequmenter;

public class SortPlaces {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws IllegalArgumentException 
	 * @throws InterruptedException 
	 * @throws ClassNotFoundException 
	 */
	public static void sortPlaces() throws IllegalArgumentException, IOException, ClassNotFoundException, InterruptedException {
		// TODO Auto-generated method stub
		
		Job job = new Job();
		job.setJarByClass(SortPlaces.class);
		job.setJobName("sortPlaces");
		
		FileInputFormat.addInputPath(job, new Path("output/result/part-r-00000"));
		
		
		String outputPath = "output/sort";
		
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
		
		job.setMapperClass(SortPlacesMapper.class);
//		job.setCombinerClass(TopPlacesReducer.class);	//combiner:dont change the result
		job.setReducerClass(SortPlacesReducer.class);
		
		job.setMapOutputKeyClass(KVEntity.class);
		job.setMapOutputValueClass(Text.class);
		
		job.setOutputValueClass(Text.class);
		job.setOutputKeyClass(IntWritable.class);
		
		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}

}
