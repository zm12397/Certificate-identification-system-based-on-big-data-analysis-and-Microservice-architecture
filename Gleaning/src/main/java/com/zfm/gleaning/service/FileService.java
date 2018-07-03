package com.zfm.gleaning.service;

import java.io.File;
import java.io.InputStream;

import com.zfm.gleaning.pojo.FileResourcesDO;

public interface FileService {
	boolean validateImgType(String filename);
	
	File saveFile(String filename, String path,int type);
	
	void addFileResources(FileResourcesDO file);
	
	FileResourcesDO getFileResources(Integer id);

	String generateValidationImage(String filePath);

	String distinguish(File localFile);
}
