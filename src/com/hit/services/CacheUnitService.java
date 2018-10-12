package com.hit.services;

import java.io.Serializable;
import java.util.HashMap;

import com.hit.dao.DaoFileImpl;
import com.hit.dao.IDao;
import com.hit.dm.DataModel;
import com.hit.memory.CacheUnit;
import com.hit.util.RequestStatistics;
import com.hit.algorithm.LRUAlgoCacheImpl;

public class CacheUnitService<T> {
	private final int CAPACITY = 10;
	private final int CACHE_CAPACITY = 15;
	private final String DEL = "delete";
	private final String UP = "update";
	private final String GET = "get";
	private CacheUnit<T> cacheUnit = new CacheUnit<>(new LRUAlgoCacheImpl<>(CAPACITY));
	private IDao<Serializable, T> dao2;
	private DaoFileImpl<T> dao = new DaoFileImpl<>("DataSource.txt"); // should i use IDAO instead?
	private DataModel<T> dataModel = null;
	private HashMap<String, String> unitStats = new HashMap<>();

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
				dao.delete(dataModel);
				System.out.println("deleting model : " + dataModel.getDataModelId());
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
		for (int i = 0; i < dataModels.length; i++) {
			ids[i] = dataModels[i].getDataModelId();
		}
		models = cacheUnit.getDataModels(ids);
		if (models == null || models.length < dataModels.length) { // if the models are not inside the cache
			for (int i = 0; i < ids.length; i++) {
				models[i] = (DataModel<T>) dao.find(ids[i]); // get from file
			}
			if (models != null) {
				cacheUnit.putDataModels(models); // if we already took them from the file , we will save inside the
													// cache
				RequestStatistics.getInstance().addSwapNum(models.length);
			}
		}
		return models;

	}

	public boolean update(DataModel<T>[] dataModels) {
		boolean isUpdated = false;
		DataModel<T>[] cacheModels = null;
		Long ids[] = new Long[dataModels.length];
		RequestStatistics.getInstance().addModels(dataModels.length);
		for (int i = 0; i < dataModels.length; i++) { // first we create an id's array
			ids[i] = dataModels[i].getDataModelId();
			System.out.println("the id is: " + ids[i]);
		}
		cacheModels = cacheUnit.getDataModels(ids); // we check if it exists in cache

		if (cacheModels.length > 0 && cacheModels != null) {
			for (int i = 0; i < cacheModels.length; i++) {
				if (cacheModels[i].getContent() != null) {
					if ((cacheModels[i].getContent()) != (dataModels[i].getContent())) {
						isUpdated = true; // if one of the values is not same as cache values then its updated
						System.out.println("model updated");
					}
					isUpdated = true;
				}
			}
		}
		cacheUnit.putDataModels(dataModels);

		System.out.println("updated status is : " + isUpdated);
		return isUpdated;

	}

	// this method will collect the statistics for the current request and will
	// deploy it to a map

	public HashMap<String, String> getUnitStatistics() {

		//unitStats.put("action", action);
		//unitStats.put("algo", algoName);
	//	unitStats.put("capacity", String.valueOf(CACHE_CAPACITY));
	//	unitStats.put("reqNum", String.valueOf());
		//unitStats.put("modelsNum", String.valueOf(modelsNum));
		//unitStats.put("extra", String.valueOf(answer));

		return unitStats;

	}

}
