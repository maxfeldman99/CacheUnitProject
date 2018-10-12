package com.hit.memory;

import com.hit.algorithm.IAlgoCache;
import com.hit.dm.DataModel;
import com.hit.util.RequestStatistics;

import java.lang.Long;

public class CacheUnit<T> {

	private IAlgoCache<Long, DataModel<T>> iAlgoCache;

	public CacheUnit(IAlgoCache<Long, DataModel<T>> algo) {
		this.iAlgoCache = algo;
	}

	public DataModel<T>[] getDataModels(Long[] ids) {

		DataModel<T>[] models = new DataModel[ids.length];

		DataModel<T> dataModel;

		for (int i = 0; i < ids.length; i++) {

			// if(iAlgoCache.getElement(ids[i])!=null) {
//				models[i].setContent((T) iAlgoCache.getElement(ids[i]));
//				models[i].setDataModelId(ids[i]);
//			}else {
//				models[i].setContent(null);
//				models[i].setDataModelId(ids[i]);;
//			}
			// dataModel.setContent(iAlgoCache.getElement(ids[i]));
			dataModel = (DataModel<T>) iAlgoCache.getElement(ids[i]); // if we have an id so we get it by id
			if (dataModel != null) { // if in cache
				models[i] = dataModel;
			}
		}

		return models;
	}

	public DataModel<T>[] putDataModels(DataModel<T>[] datamodels) {
		DataModel<T> myModel;
		int nullCounter = 0;
		for (int i = 0; i < datamodels.length; i++) {

			myModel = iAlgoCache.putElement(datamodels[i].getDataModelId(), datamodels[i]);
			if (myModel == null) { // if already was inside the cache or cache is not full
				nullCounter++;
			}else {
				RequestStatistics.getInstance().incrementSwapNum(); // adding the needed data to the statistics class
			}

		}
		if (nullCounter == datamodels.length) { // if all of the models were already inside the cache or cache was never
												// full
			return null;
		}
		return datamodels;
	}

	public void removeDataModels(Long[] ids) {
		DataModel<T> dataModel;

		for (int i = 0; i < ids.length; i++) {
			dataModel = iAlgoCache.getElement(ids[i]);
			iAlgoCache.removeElement(ids[i]);

		}
	}

}
