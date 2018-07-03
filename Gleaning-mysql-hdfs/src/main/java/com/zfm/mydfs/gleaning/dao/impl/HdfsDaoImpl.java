package com.zfm.mydfs.gleaning.dao.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.util.Progressable;
import org.springframework.stereotype.Repository;

import com.zfm.mydfs.gleaning.dao.HdfsDao;

@Repository
public class HdfsDaoImpl implements HdfsDao {

	@Override
	public void write(String uri, String text) throws IOException {
		// TODO Auto-generated method stub
		System.out.println("write" + uri);
		Configuration conf = new Configuration();
		FileSystem fs = null;
		ByteArrayInputStream tInputStringStream = new ByteArrayInputStream(text.getBytes());
		try {
			fs = FileSystem.get(URI.create(uri), conf);
			OutputStream out = fs.create(new Path(uri), new Progressable() {

				@Override
				public void progress() {
					// TODO Auto-generated method stub
					System.out.print(".");
				}
			});
			// true就自动关闭流了
			IOUtils.copyBytes(tInputStringStream, out, 4096, true);
		}finally{
			fs.close();
		}
	}

	@Override
	public void read(String uri,OutputStream out) throws IOException {
		// TODO Auto-generated method stub
		System.out.println("read" + uri);
		Configuration conf = new Configuration();
		FileSystem fs = null;
		InputStream in = null;
		try {
			fs = FileSystem.get(URI.create(uri), conf);
			in = fs.open(new Path(uri));
			IOUtils.copyBytes(in, out, 4096, false);
		} finally {
			IOUtils.closeStream(in);
			fs.close();
		}
		
	}

	@Override
	public void append(String uri, String text) throws IOException {
		// TODO Auto-generated method stub
		System.out.println("append" + uri);
		Configuration conf = new Configuration();
		conf.set("dfs.client.block.write.replace-datanode-on-failure.policy", "NEVER");
		conf.set("dfs.client.block.write.replace-datanode-on-failure.enable", "true");
		FileSystem fs = null;
		ByteArrayInputStream tInputStringStream = new ByteArrayInputStream(text.getBytes());
		try {
			fs = FileSystem.get(URI.create(uri), conf);
			OutputStream out = fs.append(new Path(uri));
			// true就自动关闭流了
			IOUtils.copyBytes(tInputStringStream, out, 4096, true);
		}finally{
			fs.close();
		}
	}

	@Override
	public boolean check(String lAST_URI) throws IllegalArgumentException, IOException {
		// TODO Auto-generated method stub
		System.out.println("check" + lAST_URI);
		boolean flag = false;
		Configuration conf = new Configuration();
		FileSystem fs = null;
		try {
			fs = FileSystem.get(URI.create(lAST_URI),conf);
			flag = fs.exists(new Path(lAST_URI));
		}finally{
			fs.close();
		}
		return flag;
	}

}
