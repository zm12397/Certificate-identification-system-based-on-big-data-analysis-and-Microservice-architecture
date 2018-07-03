package com.zfm.gleaning.kmeans.pojo;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.WritableComparable;

public class LngAndLatEntity implements WritableComparable<LngAndLatEntity> {
	private String lng;
	private String lat;

	public double OSLength(LngAndLatEntity other) {
		double len = 0.0;
		double x1 = Double.parseDouble(lng);
		double y1 = Double.parseDouble(lat);
		double x2 = Double.parseDouble(other.getLng());
		double y2 = Double.parseDouble(other.getLat());
		len = Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
		return len;
	}

	public String getLng() {
		return lng;
	}

	public void setLng(String lng) {
		this.lng = lng;
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public LngAndLatEntity() {
		super();
	}

	public LngAndLatEntity(String lng, String lat) {
		super();
		this.lng = lng;
		this.lat = lat;
	}

	@Override
	public String toString() {
		return lng + "\t" + lat;
	}

	@Override
	public void readFields(DataInput in) throws IOException {
		// TODO Auto-generated method stub
		lng = in.readUTF();
		lat = in.readUTF();
	}

	@Override
	public void write(DataOutput out) throws IOException {
		// TODO Auto-generated method stub
		out.writeUTF(lng);
		out.writeUTF(lat);
	}

	@Override
	public int compareTo(LngAndLatEntity arg0) {
		// TODO Auto-generated method stub
		return 0;
	}
}
