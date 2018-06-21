package com.zfm.mydfs.gleaning.pojo;

public class JWSourceDTO {
	private String status;
	private JWResultDTO result;
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public JWResultDTO getResult() {
		return result;
	}
	public void setResult(JWResultDTO result) {
		this.result = result;
	}
	@Override
	public String toString() {
		return "JWSourceDTO [status=" + status + ", result=" + result + "]";
	}
	public JWSourceDTO(String status, JWResultDTO result) {
		super();
		this.status = status;
		this.result = result;
	}
	public JWSourceDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}
