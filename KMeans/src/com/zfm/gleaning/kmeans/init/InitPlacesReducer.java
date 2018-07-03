package com.zfm.gleaning.kmeans.init;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.commons.math3.util.MultidimensionalCounter.Iterator;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.Reducer.Context;

import com.google.common.collect.Iterables;
import com.zfm.gleaning.kmeans.pojo.LngAndLatEntity;

public class InitPlacesReducer extends Reducer<IntWritable,LngAndLatEntity,Text,Text> {
	public enum ReducerEnum{
		ERROR_LINE,TOTAL_LINE
	}
	@Override
	protected void reduce(IntWritable key, Iterable<LngAndLatEntity> values,
			Context context)
			throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		
		int k = Integer.parseInt(context.getConfiguration().get("k"));
		for(LngAndLatEntity value : values){
			int num = (int) context.getCounter(ReducerEnum.TOTAL_LINE).getValue();
			if(num < k){
				context.write(new Text(value.getLng()),new Text(value.getLat()));
				context.getCounter(ReducerEnum.TOTAL_LINE).increment(1);
			}
		}
		
	}
	
}
