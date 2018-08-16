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

	CacheUnit(IAlgoCache<Long, DataModel<T>> algo, IDao<Serializable, DataModel<T>> dao) {
		this.iAlgoCache = algo;
		this.dao = dao;
	}

	public DataModel<T>[] getDataModels(Long[] ids) throws ClassNotFoundException, IOException {
		
		DataModel<T>[] models = new DataModel[ids.length];
		DataModel<T> dataModel;
		
		for(int i=0;i<ids.length;i++) {
			dataModel = iAlgoCache.getElement(ids[i]); // if we have an id so we get it by id
			
			if(dataModel!=null) {
				models[i] = dataModel; //add to my arrayList
			}else {
				dataModel = dao.find(ids[i]); // if not exist so we search for it by id
				iAlgoCache.putElement(dataModel.getDataModelId(), dataModel);//and we use it by paging algorithm
				
				
				//  if cash is not full return the data model
				// else make the logic for the full cache
				// if we received something that not null , so we had a page fault and we need to update the file 
				
				dao.save(dataModel);
				models[i] = dataModel;
				
				
			}
			
		}
		
	
		
		
		
		
//	do If IAlgoCache  ids // only touch the datamodels in case don’t exist
//
//	If cache is not full
//	Retrieves page by the DAO// DM Faults
//	Else
//	Do logic of full Cache // DM replacements
//
//	end;
//	return dataModels [] // from Cache

		return models;

	}

	public DataModel<T>[] putDataModels(DataModel<T>[] datamodels) {

		return datamodels;

	}

	public void removeDataModels(Long[] ids) {

	}

}
