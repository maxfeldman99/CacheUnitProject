package com.max.dm;

import java.io.Serializable;
import java.lang.Long;

public class DataModel<T> implements Serializable {
	private Long id;
	private T content;

	public DataModel(Long id, T content) {
		this.id = id;
		this.content = content;
	}

	public void setDataModelId(Long id) {
		this.id = id;
	}

	public Long getDataModelId() {
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

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof DataModel<?>)) {
			return false;
		}

		DataModel<?> otherModel = (DataModel<?>) obj;

		if (this.id != otherModel.id) {
			return false;
		}

		return true;

	}
	

	@Override
	public int hashCode() { // i'm using prime numbers to hash it efficiently
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}


}
