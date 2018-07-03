package com.zfm.gleaning.kmeans;

import java.io.IOException;
import java.util.List;

import com.zfm.gleaning.kmeans.devide.DevidePlaces;
import com.zfm.gleaning.kmeans.init.InitPlaces;
import com.zfm.gleaning.kmeans.pojo.LngAndLatEntity;

public class AppRunner {

	/**
	 * @param args
	 * @throws InterruptedException 
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 * @throws IllegalArgumentException 
	 */
	public static void main(String[] args) throws IllegalArgumentException, ClassNotFoundException, IOException, InterruptedException {
		// TODO Auto-generated method stub
		InitPlaces.initPlaces(5);
//		String str = DevidePlaces.readCenter().toString().replace("[", "").replace("]", "");
//		System.out.println(str);
		DevidePlaces.devidePlaces(5,1);
	}
	

}
