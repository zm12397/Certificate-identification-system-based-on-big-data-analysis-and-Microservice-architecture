package com.zfm.hadoop.mapper.gleaning.top;

import java.io.IOException;
import java.util.List;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Mapper;

import com.zfm.hadoop.mapper.gleaning.top.utils.WordSequmenter;


public class TopPlacesMapper extends
		Mapper<LongWritable, Text, Text, IntWritable> {
//	private static final int MISSING = 9999;
	
	@Override
	public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		String line = value.toString();
		List<String>  places = WordSequmenter.split(line);
		for(String place : places){
			context.write(new Text(place), new IntWritable(1));
		}
	}


}
