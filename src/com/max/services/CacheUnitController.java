package com.max.services;

import com.max.dm.DataModel;

public class CacheUnitController<T> {
	
	private CacheUnitService<T> cacheUnitService = new CacheUnitService<>(); // ?
	
	public CacheUnitController(){
		
	}
	
	public boolean delete(DataModel<T>[] dataModels) {
		return cacheUnitService.delete(dataModels);
	}
	
	public DataModel<T>[] get(DataModel<T>[] dataModels) {
		return cacheUnitService.get(dataModels);
		
	}
	
	public boolean update(DataModel<T>[] dataModels) {
		return cacheUnitService.update(dataModels);
		
	}
}
