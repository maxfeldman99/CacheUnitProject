package com.hit.services;

import java.io.Serializable;

import com.hit.dao.IDao;
import com.hit.dm.DataModel;
import com.hit.memory.CacheUnit;

public class CacheUnitService<T> {

	private CacheUnit<T> cacheUnit;
	private IDao<Serializable, T> dao;
	private DataModel<T> dataModel = null;

	public CacheUnitService() {

	}

	boolean delete(DataModel<T>[] dataModels) {
		boolean isDeleted = false;
		Long ids[] = new Long[dataModels.length];

		if (ids.length > 0) { // removing from file
			for (int i = 0; i < dataModels.length; i++) {
				dataModel = dataModels[i];
				dao.delete((T) dataModel);
				ids[i] = dataModel.getDataModelId();
			}
			cacheUnit.removeDataModels(ids); // removing from cache
			isDeleted = true;
		}
		return isDeleted;
	}

	public DataModel<T>[] get(DataModel<T>[] dataModels) {

		DataModel<T>[] models = null;
		Long ids[] = new Long[dataModels.length];
		for (int i = 0; i < dataModels.length; i++) {
			ids[i] = dataModels[i].getDataModelId();
		}
		models = cacheUnit.getDataModels(ids);
		if (models == null || models.length < dataModels.length) { // if the models are not inside the cache
			for (int i = 0; i < ids.length; i++) {
				models[i] = (DataModel<T>) dao.find(ids[i]); // get from file
			}
			cacheUnit.putDataModels(models); // if we already took them from the file , we will save inside the cache
		}
		return models;

	}

	public boolean update(DataModel<T>[] dataModels) {
		boolean isUpdated = false;
		DataModel<T>[] cacheModels = null;
		Long ids[] = new Long[dataModels.length];

		for (int i = 0; i < dataModels.length; i++) {
			ids[i] = dataModels[i].getDataModelId();
		}
		cacheModels = cacheUnit.getDataModels(ids);

		for (int i = 0; i < cacheModels.length; i++) {
			if (cacheModels[i].getContent() != dataModels[i].getContent()) { 
				isUpdated = true;  // if one of the values is not same as cache values then its updated
			}
		}
		cacheUnit.putDataModels(dataModels);

		return isUpdated;

	}
	
	public void getInfo() {
		
	}

}
