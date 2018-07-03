package com.zfm.gleaning.util;

import java.util.Random;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

public class CodeUtils {
	private static char[] codeSequence = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',  
            'K', 'L', 'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',  
            'X', 'Y', 'Z', '1', '2', '3', '4', '5', '6', '7', '8', '9'};  
	
	public  static String generateCode(int length){
		StringBuffer code = new StringBuffer();
		Random rand = new Random();
		for(int i = length;i > 0;i --){
			int r = rand.nextInt(codeSequence.length);
			code.append(codeSequence[r]);
		}
		return code.toString();
	}
	
	/**
	 * 生成指定次数的md5加密后的编码值
	 * @param source
	 * @param salt
	 * @param hashIterations
	 * @return
	 */
	public static String generateMD5Code(String source,String salt,int hashIterations){
		String result = null;
		result = new SimpleHash("MD5",source,ByteSource.Util.bytes(salt),hashIterations).toHex(); 
		return result;
	}
	
	/**
	 * 根据前缀和随机长度生成盐值
	 * @param prex
	 * @param randomLength
	 * @return
	 */
	public static String generateSalt(String prex,int randomLength){
		StringBuffer sb = new StringBuffer();
		Random rand = new Random();
		for(int i = randomLength;i > 0;i --){
			int r = rand.nextInt(codeSequence.length);
			sb.append(codeSequence[r]);
		}
		return prex + sb.toString();
	}
}
