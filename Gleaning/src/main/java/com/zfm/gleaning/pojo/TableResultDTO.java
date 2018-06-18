package com.zfm.gleaning.pojo;

public class TableResultDTO {
	private String code;
	private String message;
	private Object rows;
	private Long total;
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
	public Object getRows() {
		return rows;
	}
	public void setRows(Object rows) {
		this.rows = rows;
	}
	public Long getTotal() {
		return total;
	}
	public void setTotal(Long total) {
		this.total = total;
	}
	@Override
	public String toString() {
		return "TableResultDTO [code=" + code + ", message=" + message + ", rows=" + rows + ", total=" + total + "]";
	}
	public TableResultDTO(String code, String message, Object rows, Long total) {
		super();
		this.code = code;
		this.message = message;
		this.rows = rows;
		this.total = total;
	}
	public TableResultDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
	
}
