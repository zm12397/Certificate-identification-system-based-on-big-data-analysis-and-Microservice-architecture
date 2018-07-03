package com.zfm.mydfs.gleaning.pojo;

public class SourceDTO {
	private String status;
	private ResultDTO result;
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public ResultDTO getResult() {
		return result;
	}
	public void setResult(ResultDTO result) {
		this.result = result;
	}
	@Override
	public String toString() {
		return "SourceDTO [status=" + status + ", result=" + result + "]";
	}
	public SourceDTO(String status, ResultDTO result) {
		super();
		this.status = status;
		this.result = result;
	}
	public SourceDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}
