package com.zfm.hadoop.mapper.gleaning.sort;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.Reducer.Context;

import com.zfm.hadoop.mapper.gleaning.sort.pojo.KVEntity;

public class SortPlacesReducer extends Reducer<KVEntity,Text,Text,IntWritable> {
	public enum ReducerEnum{
		ERROR_LINE,TOTAL_LINE
	}
	@Override
	protected void reduce(KVEntity key, Iterable<Text> values,
			Context context)
			throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		
		for(Text value : values){
			if(context.getCounter(ReducerEnum.TOTAL_LINE).getValue() >= 10){
				return;
			}
			context.write(value,new IntWritable(key.getValue()));
			context.getCounter(ReducerEnum.TOTAL_LINE).increment(1);
		}
		
	}
	
}
