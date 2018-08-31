package com.max.services;
import java.io.Serializable;

import com.max.dao.IDao;
import com.max.dm.DataModel;
import com.max.memory.CacheUnit;


public class CacheUnitService<T> {
	
	private CacheUnit<T> cacheUnit;
	private IDao<Serializable, T> dao;
	private DataModel<T> dataModel = null;
	
	public CacheUnitService() {
		
	}
	
	boolean delete(DataModel<T>[] dataModels) {
		long ids[] = new long[dataModels.length];
		
		for (int i = 0; i < dataModels.length; i++) {
			dataModel = dataModels[i];
			dao.delete((T) dataModel);
			ids[i] = dataModel.getDataModelId();
			dataModel.setContent(null);
			
		}
		cacheUnit.removeDataModels(ids);
		
		return false;
	}
	
	DataModel<T>[] get(DataModel<T>[] dataModels){
		return dataModels;
		
	}  
	boolean update(DataModel<T>[] dataModels) {
		return false;
		
	}
	

}
