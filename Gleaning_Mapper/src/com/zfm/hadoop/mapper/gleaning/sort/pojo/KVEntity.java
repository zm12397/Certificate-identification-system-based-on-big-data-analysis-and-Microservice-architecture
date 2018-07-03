package com.zfm.hadoop.mapper.gleaning.sort.pojo;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.WritableComparable;

public class KVEntity implements WritableComparable<KVEntity>{
	private Integer value;
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	public KVEntity(int value) {
		super();
		this.value = value;
	}
	public KVEntity() {
		super();
		// TODO Auto-generated constructor stub
	}
	@Override
	public void readFields(DataInput in) throws IOException {
		// TODO Auto-generated method stub
		value = in.readInt();
	}
	@Override
	public void write(DataOutput out) throws IOException {
		// TODO Auto-generated method stub
		out.writeInt(value);
	}
	@Override
	public int compareTo(KVEntity other) {
		// TODO Auto-generated method stub
		return -this.value.compareTo(other.getValue());
	}
	
}
