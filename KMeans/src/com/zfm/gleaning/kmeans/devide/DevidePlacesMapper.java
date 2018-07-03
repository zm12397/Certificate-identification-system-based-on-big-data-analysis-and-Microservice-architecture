package com.zfm.gleaning.kmeans.devide;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Mapper;

import com.zfm.gleaning.kmeans.init.InitPlacesReducer.ReducerEnum;
import com.zfm.gleaning.kmeans.pojo.LngAndLatEntity;


public class DevidePlacesMapper extends
		Mapper<LongWritable, Text, IntWritable, LngAndLatEntity> {


	@Override
	public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		String str = context.getConfiguration().get("lals").replace("[", "").replace("]", "");
		List<LngAndLatEntity> centers= new ArrayList();
		for(String s : str.split(",")){
			String[] ss = s.trim().split("\t");
			centers.add(new LngAndLatEntity(ss[0],ss[1]));
		}
//		System.out.println(value);
		String[] pointStr = value.toString().split(",");
		LngAndLatEntity pointEntity = new LngAndLatEntity(pointStr[0].trim(),pointStr[1].trim());
		double min = Double.MAX_VALUE;
		int minIndex = 0;
		for(int i = 0;i < centers.size();i ++){
			LngAndLatEntity center = centers.get(i);
			double len = pointEntity.OSLength(center);
			if(len < min){
				min = len;
				minIndex = i;
			}
		}
		context.write(new IntWritable(minIndex), pointEntity);
	}

}
