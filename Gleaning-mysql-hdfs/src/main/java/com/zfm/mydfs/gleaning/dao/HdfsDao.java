package com.zfm.mydfs.gleaning.dao;

import java.io.IOException;
import java.io.OutputStream;

public interface HdfsDao {
	public void write(String uri,String text) throws IOException;
	public void read(String uri,OutputStream out) throws IOException;
	public void append(String uri, String string) throws IOException;
	public boolean check(String lAST_URI) throws IllegalArgumentException, IOException;
}
