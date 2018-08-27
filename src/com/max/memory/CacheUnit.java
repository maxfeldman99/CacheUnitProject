package com.max.memory;

import com.max.algorithm.IAlgoCache;
import com.max.dao.IDao;
import com.max.dm.DataModel;

import java.io.IOException;
import java.io.Serializable;
import java.lang.Long;
import java.util.ArrayList;
import java.util.Iterator;

public class CacheUnit<T> {

	private IAlgoCache<Long, DataModel<T>> iAlgoCache;
	private IDao<Serializable, DataModel<T>> dao;

	public CacheUnit(IAlgoCache<Long, DataModel<T>> algo, IDao<Serializable, DataModel<T>> dao) {
		this.iAlgoCache = algo;
		this.dao = dao;
	}

	public DataModel<T>[] getDataModels(Long[] ids) throws ClassNotFoundException, IOException {

		DataModel<T>[] models = new DataModel[ids.length];
		DataModel<T> dataModel;

		for (int i = 0; i < ids.length; i++) {
			dataModel = iAlgoCache.getElement(ids[i]); // if we have an id so we get it by id

//			if(dataModel==null) {
//				dataModel = dao.find(ids[i]);
//				if(dataModel != null) {
//					iAlgoCache.putElement(dataModel.getDataModelId(), dataModel);
//					dao.save(dataModel);
//				}
//				models[i] = dataModel;
//			}

			if (dataModel != null) { // if in cache
				models[i] = dataModel; 
			} else { // else get if from file by dao

				if (dao.find(ids[i]) == null) { // this option is not relevant but i'm using it just for safety
					models[i] = null; 
				}
				if(dao.find(ids[i])!=null){
					dataModel = dao.find(ids[i]);
					dao.save(dataModel);
					iAlgoCache.putElement(dataModel.getDataModelId(), dataModel);  // and save it back to cache
					models[i] = dataModel;
				}

			}

		}

		return models;
	}

	public DataModel<T>[] putDataModels(DataModel<T>[] datamodels) {
		DataModel<T> myModel;
		int nullCounter = 0;
		for (int i = 0; i < datamodels.length; i++) {
			
			myModel = iAlgoCache.putElement(datamodels[i].getDataModelId(), datamodels[i]);
			if(myModel == null) {
				nullCounter++;
			}
			dao.save(datamodels[i]);
		}
		if(nullCounter == datamodels.length) {
			return null;
		}
		return datamodels;
	}

	public void removeDataModels(Long[] ids) {
		DataModel<T> dataModel;

		for (int i = 0; i < ids.length; i++) {
			dataModel = iAlgoCache.getElement(ids[i]);
			iAlgoCache.removeElement(ids[i]);

			if (dataModel != null) {
				//dao.delete(dataModel);
			}
		}
	}

}
