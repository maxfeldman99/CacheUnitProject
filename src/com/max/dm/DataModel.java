package com.max.dm;

import java.io.Serializable;
import java.lang.Long;

public class DataModel<T> extends Object implements Serializable
 {
	private Long id;
	private T content;
	
	public DataModel(Long id,T content) {
		this.id = id;
		this.content = content;
	}

	@Override
	public boolean equals(Object arg0) {
		// TODO Auto-generated method stub
		return super.equals(arg0);
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return super.hashCode();
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString();
	}
	
	public void setDataModelId(Long id) {
		this.id = id;
	}
	
	public Long getDataModelId(){
		return id;
	}

	public T getContent() {
		return content;
	}

	public void setContent(T content) {
		this.content = content;
	}
	

}
