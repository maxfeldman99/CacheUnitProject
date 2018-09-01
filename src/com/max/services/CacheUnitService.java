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
		boolean isDeleted;
		
		Long ids[] = new Long[dataModels.length];

		if (ids.length > 0) { // removing from file
			for (int i = 0; i < dataModels.length; i++) {
				dataModel = dataModels[i];
				dao.delete((T) dataModel);
				// ids[i] = dataModel.getDataModelId();
				// dataModel.setContent(null);

			}
			cacheUnit.removeDataModels(ids); // removing from cache
		}
		return false;
	}

	public DataModel<T>[] get(DataModel<T>[] dataModels) {

		DataModel<T>[] models = null;
		Long ids[] = new Long[dataModels.length];

		for (int i = 0; i < dataModels.length; i++) {
			ids[i] = dataModels[i].getDataModelId();
		}

		models = cacheUnit.getDataModels(ids);
		return models;

	}

	public boolean update(DataModel<T>[] dataModels) {
		boolean isUpdated = false;
		DataModel<T>[] dataModelsUpdated = null;
		
		dataModelsUpdated = cacheUnit.putDataModels(dataModels);
		if(dataModelsUpdated!=null) {
			isUpdated = true;
		}
		
		return isUpdated;

	}

	private Long[] getModelIds(DataModel<T>[] dataModels) {

		Long ids[] = new Long[dataModels.length];

		for (int i = 0; i < dataModels.length; i++) {
			ids[i] = dataModels[i].getDataModelId();
		}
		return ids;

	}

}
