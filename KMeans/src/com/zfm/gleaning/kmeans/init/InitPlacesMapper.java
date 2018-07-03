package com.zfm.gleaning.kmeans.init;

import java.io.IOException;
import java.util.List;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Mapper;

import com.zfm.gleaning.kmeans.init.InitPlacesReducer.ReducerEnum;
import com.zfm.gleaning.kmeans.pojo.LngAndLatEntity;


public class InitPlacesMapper extends
		Mapper<LongWritable, Text, IntWritable, LngAndLatEntity> {


	@Override
	public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		
		String[] lal = value.toString().split(",");
		if(lal.length == 2){
			LngAndLatEntity lalEntity = new LngAndLatEntity(lal[0],lal[1]);
			context.write(new IntWritable(0), lalEntity);
		}
	}

}
