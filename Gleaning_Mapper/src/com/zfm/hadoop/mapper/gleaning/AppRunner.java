package com.zfm.hadoop.mapper.gleaning;

import java.io.IOException;

import com.zfm.hadoop.mapper.gleaning.sort.SortPlaces;
import com.zfm.hadoop.mapper.gleaning.top.TopPlaces;

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
		TopPlaces.topPlaces();
		SortPlaces.sortPlaces();
		
	}

}
