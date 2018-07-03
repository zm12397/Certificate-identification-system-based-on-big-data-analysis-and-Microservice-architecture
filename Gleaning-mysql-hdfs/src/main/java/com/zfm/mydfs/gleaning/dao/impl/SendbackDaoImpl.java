package com.zfm.mydfs.gleaning.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.zfm.mydfs.gleaning.dao.SendbackDao;
import com.zfm.mydfs.gleaning.pojo.SendBackDO;
import com.zfm.mydfs.gleaning.util.FormatUtils;

@Repository
public class SendbackDaoImpl implements SendbackDao{
	@Autowired
	private JdbcTemplate template;
	
	public List<String> readPickAddr(String now){
		String sql = "SELECT pick_addr,location FROM send_back s LEFT JOIN lost_found_location l "
				+ "ON l.id = s.location_id WHERE s.gmt_create < '" + now + "'";
		System.out.println(sql);
		List<String> places = template.query(sql,new RowMapper<String>(){
			@Override
			public String mapRow(ResultSet rs, int rowNumber) throws SQLException {
				// TODO Auto-generated method stub
				String place = rs.getString("pick_addr") == null ||  rs.getString("pick_addr").equals("") 
						? FormatUtils.formatPlace(rs.getString("location"),"招领(台|处|中心)","") : rs.getString("pick_addr");
				return place;
			}
			
		});
		return places;
	}

	@Override
	public List<String> readPickAddr(String from,String to) {
		// TODO Auto-generated method stub
		String sql = "SELECT pick_addr,location FROM send_back s LEFT JOIN lost_found_location l "
				+ "ON l.id = s.location_id WHERE s.gmt_create > '" + from + "'  AND s.gmt_create < '" + to + "'";
		System.out.println(sql);
		List<String> places = template.query(sql,new RowMapper<String>(){
			@Override
			public String mapRow(ResultSet rs, int rowNumber) throws SQLException {
				// TODO Auto-generated method stub
				String place = rs.getString("pick_addr") == null ||  rs.getString("pick_addr").equals("") 
						? FormatUtils.formatPlace(rs.getString("location"),"招领(台|处|中心)","") : rs.getString("pick_addr");
				return place;
			}
		});
		return places;
	}

	@Override
	public List<String> readAddr() {
		// TODO Auto-generated method stub
		String sql = "SELECT pick_addr,location FROM send_back s LEFT JOIN lost_found_location l "
				+ "ON l.id = s.location_id WHERE s.gmt_create < CURRENT_TIMESTAMP";
		System.out.println(sql);
		List<String> places = template.query(sql,new RowMapper<String>(){
			@Override
			public String mapRow(ResultSet rs, int rowNumber) throws SQLException {
				// TODO Auto-generated method stub
				String place = rs.getString("pick_addr") == null ||  rs.getString("pick_addr").equals("") 
						? FormatUtils.formatPlace(rs.getString("location"),"招领(台|处|中心)","") : rs.getString("pick_addr");
				return place;
			}
			
		});
		return places;
	}
	
	
}
