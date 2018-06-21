package com.zfm.mydfs.gleaning;
/**
 * 自定义全局异常
 * 	继承运行时异常，用于异常信息封装以及事务回滚
 * @author zm
 *
 */
public class CustomerException extends RuntimeException{
	private String message;
	public CustomerException(String message) {
		this.message = message;
	}

	@Override
	public String getMessage() {
		// TODO Auto-generated method stub
		return message;
	}
	
	
}