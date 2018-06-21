package com.zfm.mydfs.gleaning.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zfm.mydfs.gleaning.pojo.AddressComponentDTO;
import com.zfm.mydfs.gleaning.pojo.JWSourceDTO;
import com.zfm.mydfs.gleaning.pojo.SourceDTO;

import net.minidev.json.JSONObject;

//计算signature签名
public class SnCalUtils {
	private static final String AK = "TtnSf0FBuheEGM50l3tMLznDIsQKdler";
	private static final String SK = "s8ocU16hd4nFWg5t30P70Sl6bxISz299";
	private static final String URL_PREFIX = "http://api.map.baidu.com/geocoder/v2/?";
	public static void main(String args[]) throws IOException, NoSuchAlgorithmException{
		BufferedReader reader = new BufferedReader(new FileReader(new File("C:/Users/zm/Desktop/places.txt")));
		String str = null;
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File("C:/Users/zm/Desktop/lal.txt")));
		while((str = reader.readLine()) != null){
			JWSourceDTO source = translateAddress(str);
			writer.write(source.getResult().getLocation().toString());
			writer.newLine();//或者在write后加入\r\n
			System.out.println(source.getResult());
		}
		writer.flush();
		writer.close();
		reader.close();
		/*String[] locations = {"31.256121906224312,121.48128289878294","23.8267424633964,113.18008837725289",
		                      "22.9023234463252825,114.41709613476903","36.961612088927936,103.41579190310952",
		                      "42.08110996420624,120.8292046646019"};
		for(String str : locations){
			SourceDTO source = translateLocation(str);
			System.out.println(source);
		}
		*/
	}
	/*public static void main(String args[]) throws NoSuchAlgorithmException, JsonParseException, JsonMappingException, IOException{
		String url = "http://api.map.baidu.com/geocoder/v2/?";
		String address = "凯德广场";
		Map paramsMap = new LinkedHashMap<String, String>();
		paramsMap.put("address", address);
//		paramsMap.put("location", "45.74121633225562,126.60062415045359");
		paramsMap.put("output", "json");
		paramsMap.put("ak", AK);
		paramsMap.put("sn", SnCalUtils.getSn("address",address));
		url = url + SnCalUtils.toQueryString(paramsMap);
		System.out.println(url);
		String response = HttpRequestUtils.sendGet(url);
		System.out.println(response);
		ObjectMapper objectMapper = new ObjectMapper();
		JWSourceDTO s = objectMapper.readValue(response, JWSourceDTO.class);
		System.out.println(s);
	}*/
	public static JWSourceDTO translateAddress(String address) throws NoSuchAlgorithmException, JsonParseException, JsonMappingException, IOException{
		JWSourceDTO jwSource = null;
		StringBuffer url = new StringBuffer(URL_PREFIX);
		Map paramsMap = new LinkedHashMap<String, String>();
		paramsMap.put("address", address);
		paramsMap.put("output", "json");
		paramsMap.put("ak", AK);
		paramsMap.put("sn", SnCalUtils.getSn("address",address));
		url.append(SnCalUtils.toQueryString(paramsMap));
//		System.out.println(url);
		String response = HttpRequestUtils.sendGet(url.toString());
		System.out.println(response);
		ObjectMapper objectMapper = new ObjectMapper();
		jwSource = objectMapper.readValue(response, JWSourceDTO.class);
//		System.out.println(jwSource);
		return jwSource;
	}
	
	public static SourceDTO translateLocation(String location) throws NoSuchAlgorithmException, JsonParseException, JsonMappingException, IOException{
		SourceDTO sourceDTO = null;
		StringBuffer url = new StringBuffer(URL_PREFIX);
		Map paramsMap = new LinkedHashMap<String, String>();
//		paramsMap.put("location", "45.74121633225562,126.60062415045359");
		paramsMap.put("location", location);
		paramsMap.put("output", "json");
		paramsMap.put("ak", AK);
		paramsMap.put("sn", SnCalUtils.getSn("location",location));
		url.append(SnCalUtils.toQueryString(paramsMap));
		System.out.println(url);
		String response = HttpRequestUtils.sendGet(url.toString());
		System.out.println(response);
		ObjectMapper objectMapper = new ObjectMapper();
		sourceDTO = objectMapper.readValue(response, SourceDTO.class);
		System.out.println(sourceDTO);
		return sourceDTO;
	}
	
	public static String getSn(String addKey,String addValue) throws UnsupportedEncodingException, NoSuchAlgorithmException {
		SnCalUtils snCal = new SnCalUtils();

		// 计算sn跟参数对出现顺序有关，get请求请使用LinkedHashMap保存<key,value>，该方法根据key的插入顺序排序；post请使用TreeMap保存<key,value>，该方法会自动将key按照字母a-z顺序排序。所以get请求可自定义参数顺序（sn参数必须在最后）发送请求，但是post请求必须按照字母a-z顺序填充body（sn参数必须在最后）。以get请求为例：http://api.map.baidu.com/geocoder/v2/?address=百度大厦&output=json&ak=yourak，paramsMap中先放入address，再放output，然后放ak，放入顺序必须跟get请求中对应参数的出现顺序保持一致。

		Map paramsMap = new LinkedHashMap<String, String>();
//		paramsMap.put("address", address);
		paramsMap.put(addKey, addValue);
		paramsMap.put("output", "json");
		paramsMap.put("ak", AK);

		// 调用下面的toQueryString方法，对LinkedHashMap内所有value作utf8编码，拼接返回结果address=%E7%99%BE%E5%BA%A6%E5%A4%A7%E5%8E%A6&output=json&ak=yourak
		String paramsStr = snCal.toQueryString(paramsMap);

		// 对paramsStr前面拼接上/geocoder/v2/?，后面直接拼接yoursk得到/geocoder/v2/?address=%E7%99%BE%E5%BA%A6%E5%A4%A7%E5%8E%A6&output=json&ak=yourakyoursk
		String wholeStr = new String("/geocoder/v2/?" + paramsStr + SK);
//		System.out.println(wholeStr);
		// 对上面wholeStr再作utf8编码
		String tempStr = URLEncoder.encode(wholeStr, "UTF-8");

		// 调用下面的MD5方法得到最后的sn签名7de5a22212ffaa9e326444c75a58f9a0
		String sn = snCal.MD5(tempStr);
		System.out.println(sn);
		
		return sn;
	}

	// 对Map内所有value作utf8编码，拼接返回结果
	public static String toQueryString(Map<?, ?> data) throws UnsupportedEncodingException {
		StringBuffer queryString = new StringBuffer();
		for (Entry<?, ?> pair : data.entrySet()) {
			queryString.append(pair.getKey() + "=");
			queryString.append(URLEncoder.encode((String) pair.getValue(), "UTF-8") + "&");
		}
		if (queryString.length() > 0) {
			queryString.deleteCharAt(queryString.length() - 1);
		}
		return queryString.toString();
	}

	// 来自stackoverflow的MD5计算方法，调用了MessageDigest库函数，并把byte数组结果转换成16进制
	public String MD5(String md5) {
		try {
			java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
			byte[] array = md.digest(md5.getBytes());
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < array.length; ++i) {
				sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
			}
			return sb.toString();
		} catch (java.security.NoSuchAlgorithmException e) {
		}
		return null;
	}
}
