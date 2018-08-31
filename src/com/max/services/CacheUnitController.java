package com.max.services;

import com.max.dm.DataModel;

public class CacheUnitController<T> {

	public CacheUnitController(){
		
	}
	
	boolean delete(DataModel<T>[] dataModels) {
		return false;
	}
	
	DataModel<T>[] get(DataModel<T>[] dataModels) {
		return dataModels;
		
	}
	
	boolean update(DataModel<T>[] dataModels) {
		return false;
		
	}
}
