package com.zfm.mydfs.gleaning.dao;

import java.util.List;

public interface SendbackDao {
	public List<String> readPickAddr(String now);
	public List<String> readPickAddr(String from,String to);
	public List<String> readAddr();
}
