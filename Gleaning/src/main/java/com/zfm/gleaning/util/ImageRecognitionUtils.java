package com.zfm.gleaning.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import org.springframework.util.ClassUtils;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

public class ImageRecognitionUtils {
	
	private static final String datapath = ClassUtils.getDefaultClassLoader().getResource("").getPath().substring(1) + 
			"static/tessdata";
	private static final String certificateRegex = "(\\d{17}[0-9xX])|(\\d{15})";
	private static final String schoolNumberRegec = "\\d{10}";
	
	public static String crackImage(File imageFile) throws IOException, TesseractException {
		ITesseract instance = new Tesseract();// Tesseract对象
		instance.setDatapath(datapath);
		// 中文识别，调用chi_sim.traineddata
		// 不设置默认为只识别英文和数字，全部语言见https://github.com/tesseract-ocr/tessdata
		// instance.setLanguage("chi_sim");
		BufferedImage textImage = ImageIO.read(imageFile);
		// 通过Tesseract库预定义的doOCR方法传递
		String result = instance.doOCR(textImage);
		textImage.flush();
		return result;
	}
	
	public static String matchNumber(String text){
		System.out.println(text);
		Pattern pattern = Pattern.compile(certificateRegex);
		Matcher matcher = pattern.matcher(text);
		if(matcher.find()){
			System.out.println("find");
			return matcher.group();
		}else{
			System.out.println("no find");
			pattern = Pattern.compile(schoolNumberRegec);
			matcher = pattern.matcher(text);
			if(matcher.find()){
				System.out.println("find");
				return matcher.group();
			}
		}
		return null;
	}
}
