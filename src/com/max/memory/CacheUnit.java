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
				models[i] = dataModel; // add to my array
			} else {
				
				if (dao.find(ids[i]) == null) { // if not in cache
					iAlgoCache.putElement(dataModel.getDataModelId(), dataModel);// and we use it by paging algorithm
				} else {
					dao.save(dataModel);
					models[i] = dataModel;
				}

			}

		}

		return models;
	}

	public DataModel<T>[] putDataModels(DataModel<T>[] datamodels) {
		for (int i = 0; i < datamodels.length; i++) {

			iAlgoCache.putElement(datamodels[i].getDataModelId(), datamodels[i]);
			dao.save(datamodels[i]);
		}
		return datamodels;
	}

	public void removeDataModels(Long[] ids) {
		DataModel<T> dataModel;

		for (int i = 0; i < ids.length; i++) {
			dataModel = iAlgoCache.getElement(ids[i]);
			iAlgoCache.removeElement(ids[i]);

			if (dataModel != null) {
				dao.delete(dataModel);
			}
		}
	}

}
