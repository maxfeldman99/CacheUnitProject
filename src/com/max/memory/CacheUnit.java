package com.max.memory;

import com.max.algorithm.IAlgoCache;
import com.max.dao.IDao;
import com.max.dm.DataModel;

import java.io.IOException;
import java.io.Serializable;
import java.lang.Long;

public class CacheUnit<T> {

	private IAlgoCache iAlgoCache;
	private IDao dao;

	CacheUnit(IAlgoCache<Long, DataModel<T>> algo, IDao<Serializable, DataModel<T>> dao) {
		this.iAlgoCache = algo;
		this.dao = dao;
	}

	public DataModel<T>[] getDataModels(Long[] ids) throws ClassNotFoundException, IOException {

//	do If IAlgoCache  ids // only touch the datamodels in case don’t exist
//
//	If cache is not full
//	Retrieves page by the DAO// DM Faults
//	Else
//	Do logic of full Cache // DM replacements
//
//	end;
//	return dataModels [] // from Cache

		return null;

	}

	public DataModel<T>[] putDataModels(DataModel<T>[] datamodels) {

		return datamodels;

	}

	public void removeDataModels(Long[] ids) {

	}

}
