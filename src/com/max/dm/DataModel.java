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


	@Override
	public boolean equals(Object obj) {
		if ( obj == null  || !(obj instanceof DataModel<?>) ) 
            return false;
		DataModel<?> otherModel = (DataModel<?>) obj;
		
		 if (this.id != otherModel.id) {
		        return false;
		 }

		    return true;

		
	}


	
	
//	public boolean equals(DataModel<?> obj) {
//		//if (obj == null) return false;
//	    if (!(obj instanceof DataModel<?>))
//	        return false;
//	    if (obj.getContent() == this.getContent() && obj.getDataModelId() == this.getDataModelId()){
//	    	return true;
//	    }
//	    return false;
//
//	}
		
	

}
