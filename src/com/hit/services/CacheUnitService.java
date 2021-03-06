package com.hit.services;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.hit.dao.DaoFileImpl;
import com.hit.dao.IDao;
import com.hit.dm.DataModel;
import com.hit.memory.CacheUnit;
import com.hit.util.RequestStatistics;
import com.hit.algorithm.LRUAlgoCacheImpl;

public class CacheUnitService<T> {

	// this class will contain the logic of the dispatched requests from client

	private final int CAPACITY = 10;
	private CacheUnit<T> cacheUnit = new CacheUnit<>(new LRUAlgoCacheImpl<>(CAPACITY));
	private DaoFileImpl<T> dao = new DaoFileImpl<>("DataSource.txt"); 
	private DataModel<T> dataModel = null;

	public CacheUnitService() {
		RequestStatistics.getInstance().setAlgoName("LRU");
		RequestStatistics.getInstance().setCapacity(CAPACITY);
	}

	boolean delete(DataModel<T>[] dataModels) {
		boolean isDeleted = false;
		Long ids[] = new Long[dataModels.length];
		RequestStatistics.getInstance().addModels(dataModels.length);
		if (ids.length > 0) { // removing from file
			for (int i = 0; i < dataModels.length; i++) {
				dataModel = dataModels[i];
				dao.delete(dataModel); // removing from dao
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
		RequestStatistics.getInstance().addModels(dataModels.length);

		for (int i = 0; i < dataModels.length; i++) { // creating an id's array
			ids[i] = dataModels[i].getDataModelId();
		}

		models = cacheUnit.getDataModels(ids);

		if (models == null || models.length < dataModels.length) { // if the models are not inside the cache
			for (int i = 0; i < ids.length; i++) {
				models[i] = (DataModel<T>) dao.find(ids[i]); // get from file(HDD)
			}
			if (models != null) {
				cacheUnit.putDataModels(models); // if we already took them from the file , we will save inside the
													// cache
			}
		}
		return models;
	}

	public boolean update(DataModel<T>[] dataModels) {
		boolean isUpdated = false;
		DataModel[] cacheModels = null;
		Long ids[] = new Long[dataModels.length];
		RequestStatistics.getInstance().addModels(dataModels.length);

		for (int i = 0; i < dataModels.length; i++) { // first we create an id's array
			ids[i] = dataModels[i].getDataModelId();
		}

		cacheModels = cacheUnit.getDataModels(ids); // we check if it exists in cache

		if (cacheModels.length > 0 && cacheModels != null) {
			for (int i = 0; i < cacheModels.length; i++) {
				if (cacheModels[i].getContent() != null) {

					if ((cacheModels[i].getContent()) != (dataModels[i].getContent())) {
						isUpdated = true; // if one of the values is not same as cache values then its updated
					}
					isUpdated = true;
				}
			}
		}
		cacheUnit.putDataModels(dataModels); // since we wanted to update the values, we place them inside the cache
												// as well
		return isUpdated;

	}

	// this method will collect the statistics for the current request and will
	// deploy it to a map later inside the view class

	public String getUnitStatistics() {
		return RequestStatistics.getInstance().toString();
	}

}
