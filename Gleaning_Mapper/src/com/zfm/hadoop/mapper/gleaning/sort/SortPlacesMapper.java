package com.zfm.hadoop.mapper.gleaning.sort;

import java.io.IOException;
import java.util.List;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Mapper;

import com.zfm.hadoop.mapper.gleaning.sort.pojo.KVEntity;
import com.zfm.hadoop.mapper.gleaning.top.utils.WordSequmenter;


public class SortPlacesMapper extends
		Mapper<LongWritable, Text, KVEntity, Text> {


	@Override
	public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		
		String[] line = value.toString().split("\t");
		context.write(new KVEntity(Integer.parseInt(line[1])), new Text(line[0]));
	}

}
