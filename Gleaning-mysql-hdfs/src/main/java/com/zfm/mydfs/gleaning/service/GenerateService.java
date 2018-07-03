package com.zfm.mydfs.gleaning.service;

public interface GenerateService {
	public void initGenerate();
	public void appendGenerate();
	public boolean checkLastFile();
}
