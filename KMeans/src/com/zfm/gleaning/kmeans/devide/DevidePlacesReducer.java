package com.zfm.gleaning.kmeans.devide;

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

public class DevidePlacesReducer extends Reducer<IntWritable,LngAndLatEntity,Text,Text> {
	/*public enum ReducerEnum{
		ERROR_LINE,TOTAL_LINE
	}*/
	@Override
	protected void reduce(IntWritable key, Iterable<LngAndLatEntity> values,
			Context context)
			throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		String str = context.getConfiguration().get("lals").replace("[", "").replace("]", "");
		List<LngAndLatEntity> centers= new ArrayList();
		for(String s : str.split(",")){
			String[] ss = s.trim().split("\t");
			centers.add(new LngAndLatEntity(ss[0],ss[1]));
		}
		double x = 0.0;
		double y = 0.0;
		double n =0.0;
		List<LngAndLatEntity> newIterator = new ArrayList();
		for(LngAndLatEntity value : values){
			x += Double.parseDouble(value.getLng());
			y += Double.parseDouble(value.getLat());
			n += 1;
			newIterator.add(value);
		}
		if(n != 0){
			x = x / n;
			y = y / n;
		}
		context.write(new Text(x + ""),new Text(y + ""));
		double osLength = 0.0;
		LngAndLatEntity center = new LngAndLatEntity(x + "",y + "");
		for(LngAndLatEntity value : newIterator){
			double os = value.OSLength(center);
			osLength += os * os;
		}
		DevidePlaces.cost += osLength;
		/*System.out.println(osLength);
		double old = DevidePlaces.readCost();
		old += osLength;
		DevidePlaces.writeCost(old);*/
		/*double old = context.getConfiguration().get("os") != null ? Double.parseDouble(context.getConfiguration().get("os")) : 0;
		old += osLength;
		context.getConfiguration().set("os", old + "");*/
	}
	
}
