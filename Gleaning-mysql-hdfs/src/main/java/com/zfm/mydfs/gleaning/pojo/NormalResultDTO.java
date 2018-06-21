package com.zfm.mydfs.gleaning.pojo;

import java.io.Serializable;

public class NormalResultDTO  implements Serializable{
	private String code;
	private String message;
	private Object data;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	public NormalResultDTO(String code, String message, Object data) {
		super();
		this.code = code;
		this.message = message;
		this.data = data;
	}
	public NormalResultDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	@Override
	public String toString() {
		return "NormalResultDTO [code=" + code + ", message=" + message + ", data=" + data + "]";
	}
	
}
