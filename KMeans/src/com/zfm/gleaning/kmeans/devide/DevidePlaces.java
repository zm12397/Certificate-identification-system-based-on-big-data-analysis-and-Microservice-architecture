package com.zfm.gleaning.kmeans.devide;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URI;
import java.util.Random;
import java.util.List;
import java.util.ArrayList;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Progressable;

import org.apache.hadoop.io.Text;

import com.zfm.gleaning.kmeans.pojo.LngAndLatEntity;

public class DevidePlaces {
	public static double cost = 0.0;
	/**
	 * @param args
	 * @throws IOException 
	 * @throws IllegalArgumentException 
	 * @throws InterruptedException 
	 * @throws ClassNotFoundException 
	 */
	public static void devidePlaces(int k,int yz) throws IllegalArgumentException, IOException, ClassNotFoundException, InterruptedException {
		// TODO Auto-generated method stub
		String currentResult = "output/lal/center/part-r-00000";
		
		int circle = 0;
		String outputPath = "output/lal/result" + circle;
		
		Configuration conf = new Configuration();
		FileSystem fs = null;
		double lastCost = cost;
		while(true){
			Job job = new Job();
			job.setJarByClass(DevidePlaces.class);
			job.setJobName("devidePlaces");
			
//			FileInputFormat.addInputPath(job, new Path("output/lal/center/part-r-00000"));
			FileInputFormat.addInputPath(job, new Path("input/lal.txt"));
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
			
			List<LngAndLatEntity> lals = readCenter(currentResult);
			
			Configuration conf2 = job.getConfiguration();
			conf2.set("lals", lals.toString());
			job.setMapperClass(DevidePlacesMapper.class);
//			job.setCombinerClass(TopPlacesReducer.class);	//combiner:dont change the result
			job.setReducerClass(DevidePlacesReducer.class);
			
			job.setMapOutputKeyClass(IntWritable.class);
			job.setMapOutputValueClass(LngAndLatEntity.class);
			
			job.setOutputValueClass(Text.class);
			job.setOutputKeyClass(Text.class);
			
			job.waitForCompletion(true);
			if(Math.abs(cost-lastCost) < yz){
				System.out.println("complete!");
				break;
			}else{
				System.out.println(lastCost + "-" + cost);
				lastCost = cost;
				cost = 0.0;
				circle ++ ;
				currentResult = outputPath + "/part-r-00000";
				outputPath = "output/lal/result" + circle;
			}
			
		}
		
	}
	
	public static List<LngAndLatEntity> readCenter(String uri) throws IOException{
		List<LngAndLatEntity> list = new ArrayList();
		Configuration conf = new Configuration();
		FileSystem fs = null;
		InputStream in = null;
//		String uri = "output/lal/center/part-r-00000";
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		String str = null;
		try {
			// null
			fs = FileSystem.get(URI.create(uri), conf);
			in = fs.open(new Path(uri));
			IOUtils.copyBytes(in, out, 4096, false);
			str = new String(out.toByteArray());
		} finally {
			IOUtils.closeStream(in);
			out.close();
		}
//		System.out.println(str);
		for(String s : str.split("\n")){
			String[] ss = s.split("\t");
			list.add(new LngAndLatEntity(ss[0],ss[1]));
		}
		try {
			fs.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	public static double readCost() throws IOException{
		double cost = 0;
		Configuration conf = new Configuration();
		FileSystem fs = null;
		InputStream in = null;
		String uri = "output/lal/cost.txt";
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		String str = null;
		try {
			// null
			fs = FileSystem.get(URI.create(uri), conf);
			in = fs.open(new Path(uri));
			IOUtils.copyBytes(in, out, 4096, false);
			str = new String(out.toByteArray());
		} finally {
			IOUtils.closeStream(in);
			out.close();
		}
//		System.out.println(str);
	    if(str == null || str.equals("")){
	    	cost = 0;
	    }else{
	    	cost = Double.parseDouble(str);
	    }
		try {
			fs.close();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return cost;
	}
	public static void writeCost(double cost) {
		String uri = "output/lal/cost.txt";
		String text = cost + "";
		// TODO Auto-generated method stub
		Configuration conf = new Configuration();
		FileSystem fs = null;
		ByteArrayInputStream tInputStringStream = new ByteArrayInputStream(
				text.getBytes());
		try {
			fs = FileSystem.get(URI.create(uri), conf);
			OutputStream out = fs.create(new Path(uri), new Progressable() {

				@Override
				public void progress() {
					// TODO Auto-generated method stub
					System.out.print(".");
				}
			});
			// true就自动关闭流了
			IOUtils.copyBytes(tInputStringStream, out, 4096, true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			fs.close();
			tInputStringStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
