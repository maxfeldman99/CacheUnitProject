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


	@Override
	public String toString() {
		return "DataModel [id=" + id + ", content=" + content + "]";
	}
	

}
