package com.zfm.gleaning.service.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ClassUtils;

import com.zfm.gleaning.CustomerException;
import com.zfm.gleaning.MyTargetDataSource;
import com.zfm.gleaning.MyTargetDataSource.MyDataSourceType;
import com.zfm.gleaning.dao.FileResourcesDao;
import com.zfm.gleaning.dao.UserInfoDao;
import com.zfm.gleaning.pojo.FileResourcesDO;
import com.zfm.gleaning.pojo.UserInfoDO;
import com.zfm.gleaning.service.FileService;
import com.zfm.gleaning.util.ImageRecognitionUtils;
import com.zfm.gleaning.util.TimeUtils;
import com.zfm.gleaning.util.ValidateUtils;

import lombok.extern.slf4j.Slf4j;
import net.sourceforge.tess4j.TesseractException;

@Service
@Slf4j
@Transactional
public class FileServiceImpl implements FileService {
	@Autowired
	private FileResourcesDao fileResourcesDao;
	
	public static final String USER_IMG_UPLOAD_PATH = "/static/img/user/";
	public static final String USER_IMG_NM_PREFIX = "user_img_";
	public static final String CERTIFICATE_IMG_UPLOAD_PATH = "/static/img/certificate/";
	public static final String CERTIFICATE_IMG_NM_PREFIX = "certificate_img_";
	// 有效的文件后缀
	private static List<String> validedFileNameList = Arrays.asList("jpg", "gif", "bmp", "png", "jpeg", "doc", "docx",
			"xls", "xlsx", "ppt", "pptx", "swf", "rar", "zip", "pdf", "txt");

	// 有效的图片文件后缀
	private static List<String> validedImgFileNameList = Arrays.asList("jpg", "gif", "bmp", "png", "jpeg");

	// 是否为有效的图片文件
	public boolean validateImgType(String filename) {
		// filename = file.getOriginalFilename()
		String type = filename.substring(filename.indexOf(".") + 1).toLowerCase(); // 获取文件后缀
		if (!validedImgFileNameList.contains(type)) {
			return false;
		}
		return true;
	}

	@Override
	public File saveFile(String filename, String path,int type) {
		// TODO Auto-generated method stub
		String prefix = USER_IMG_NM_PREFIX;
		String relativePath = USER_IMG_UPLOAD_PATH;
		if(type == 1){
			prefix = CERTIFICATE_IMG_NM_PREFIX;
			relativePath = CERTIFICATE_IMG_UPLOAD_PATH;
		}
		String saveName = prefix + TimeUtils.getNow() + "." +
				filename.substring(filename.indexOf(".") + 1).toLowerCase();
		log.info(path + relativePath + saveName);
		File file = new File(path + relativePath + saveName);
		try {
			file.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
			log.error("文件资源本地创建失败：" + e.getMessage());
			throw new CustomerException("文件资源本地创建失败");
		}
		return file;
		
	}

	@Override
	@MyTargetDataSource(value = MyDataSourceType.MASTER)
	public void addFileResources(FileResourcesDO file) {
		// TODO Auto-generated method stub
//		FileResourcesDO fileResouces = new FileResourcesDO();
//		fileResouces.setUrl(path + relativePath + saveName);
		file.setState((short) 1);
		try{
			fileResourcesDao.saveAndFlush(file);
		}catch(Exception e){
			log.error("文件资源插入数据库失败：" + e.getMessage());
			throw new CustomerException("文件资源插入数据库失败");
		}
	}

	@Override
	@MyTargetDataSource(value = MyDataSourceType.SLAVE)
	public FileResourcesDO getFileResources(Integer id) {
		// TODO Auto-generated method stub
		FileResourcesDO file = null;
		try{
			file = fileResourcesDao.findOne(id);
		}catch(Exception e){
			log.error("文件资源查询失败：" + e.getMessage());
			throw new CustomerException("文件资源查询失败");
		}
		return file;
	}

	@Override
	public String generateValidationImage(String filePath) {
		// TODO Auto-generated method stub
		ValidateUtils vCode = new ValidateUtils(160,40,5,150); 
//		String path = "D:\\workspace\\web-workspace2\\Gleaning\\src\\main\\resources\\static\\" + filePath;
//		获取项目部署绝对路径ClassUtils.getDefaultClassLoader().getResource("").getPath()：D:/workspace/web-workspace2/Gleaning/target/classes/
		String path = ClassUtils.getDefaultClassLoader().getResource("").getPath() + "static\\" + filePath;
		try {
			vCode.write(path);
			return vCode.getCode();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log.error("验证码生成失败：" + e.getMessage());
			throw new CustomerException("验证码生成失败");
		}
		
		
	}

	@Override
	public String distinguish(File localFile) {
		// TODO Auto-generated method stub
		String number = "";
		try{
			String text = ImageRecognitionUtils.crackImage(localFile);
			number = ImageRecognitionUtils.matchNumber(text);
		}catch(IOException e){
			log.error("图像识别-文件输入输出错误：" + e.getMessage());
			return number;
		}catch(TesseractException e){
			log.error("图像识别Tesseract错误：" + e.getMessage());
			return number;
		}catch(Exception e){
			log.error("图像识别错误：" + e.getMessage());
			return number;
		}
		return number;
	}

	

}
