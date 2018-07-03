package com.zfm.gleaning.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.zfm.gleaning.CustomerException;
import com.zfm.gleaning.pojo.FileResourcesDO;
import com.zfm.gleaning.pojo.NormalResultDTO;
import com.zfm.gleaning.service.FileService;

import lombok.extern.slf4j.Slf4j;

/**
 * 文件操作相关controller
 * @author zm
 *
 */
@RestController
@RequestMapping("/file")
@Slf4j
public class FileController {
	@Autowired
	private FileService fileService;

	@RequestMapping(value = "/head/upload.action", method = RequestMethod.POST)
	public NormalResultDTO headUpload(MultipartFile file, HttpServletRequest request) {
		NormalResultDTO result = new NormalResultDTO("9999", "unknown error", null);
		// 先验证图片格式
		String filename = file.getOriginalFilename(); // 上传时的文件名
		if (!fileService.validateImgType(filename)) { // 图片格式错误
			result.setMessage("该文件不是图片格式");
			return result;
		}
		// 获取项目所在绝对路径
		String path = ClassUtils.getDefaultClassLoader().getResource("")
				.getPath()/* "D:\\workspace\\web-workspace2\\Gleaning\\src\\main\\resources" */;
		File localFile = null;
		try {
			localFile = fileService.saveFile(filename, path, 0);
		} catch (CustomerException e) {
			result.setMessage(e.getMessage());
			return result;
		}
		try {
			file.transferTo(localFile);
		} catch (IllegalStateException e1) {
			// TODO Auto-generated catch block
			log.error(e1.getMessage());
			result.setMessage("文件状态异常");
			return result;
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			log.error(e1.getMessage());
			result.setMessage("文件读写异常");
			return result;
		}
		FileResourcesDO fileResources = new FileResourcesDO();
		log.info(localFile.getName());
		fileResources.setUrl(localFile.getName());
		try {
			fileService.addFileResources(fileResources);
		} catch (CustomerException e) {
			result.setMessage(e.getMessage());
			return result;
		}
		if (fileResources.getId() != null && fileResources.getId() != 0) {
			result.setCode("0000");
			result.setMessage("上传成功");
			result.setData(fileResources);
		}
		log.info(result.toString());
		return result;
	}
	
	// 图片上传
	// type 0是头像，1是证件
	@RequestMapping(value = "/img/upload.action", method = RequestMethod.POST)
	public NormalResultDTO imgUpload(MultipartFile file, HttpServletRequest request) {
		NormalResultDTO result = new NormalResultDTO("9999", "unknown error", null);
		// 先验证图片格式
		String filename = file.getOriginalFilename(); // 上传时的文件名
		if (!fileService.validateImgType(filename)) { // 图片格式错误
			result.setMessage("该文件不是图片格式");
			return result;
		}
		// 获取项目所在绝对路径
		String path = ClassUtils.getDefaultClassLoader().getResource("")
				.getPath()/* "D:\\workspace\\web-workspace2\\Gleaning\\src\\main\\resources" */;
		File localFile = null;
		try {
			localFile = fileService.saveFile(filename, path, 1);
		} catch (CustomerException e) {
			result.setMessage(e.getMessage());
			return result;
		}
		try {
			file.transferTo(localFile);
		} catch (IllegalStateException e1) {
			// TODO Auto-generated catch block
			log.error(e1.getMessage());
			result.setMessage("文件状态异常");
			return result;
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			log.error(e1.getMessage());
			result.setMessage("文件读写异常");
			return result;
		}
		FileResourcesDO fileResources = new FileResourcesDO();
		log.info(localFile.getName());
		fileResources.setUrl(localFile.getName());
		try {
			fileService.addFileResources(fileResources);
		} catch (CustomerException e) {
			result.setMessage(e.getMessage());
			return result;
		}
		if (fileResources.getId() != null && fileResources.getId() != 0) {
			result.setCode("0000");
			String number = fileService.distinguish(localFile);
			result.setMessage(number);
			result.setData(fileResources);
		}
		log.info(result.toString());
		return result;
	}

}
