package com.zfm.hadoop.mapper.gleaning.top.utils;

import java.util.List;

import com.huaban.analysis.jieba.JiebaSegmenter;
import com.huaban.analysis.jieba.JiebaSegmenter.SegMode;

public class WordSequmenter {
	public static List<String> split(String text){
		JiebaSegmenter segmenter = new JiebaSegmenter();
		List<String> result = segmenter.sentenceProcess(text);
		return result;
	}
}
