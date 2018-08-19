package com.max.memory;


import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.io.IOException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import com.max.algorithm.LRUAlgoCacheImpl;
import com.max.dao.DaoFileImpl;
import com.max.dm.DataModel;

public class CacheUnitTest {

	private static final int CAPACITY = 10;
	//private Integer randomNum = (int) (Math.random() * CAPACITY);
	

	
	@Test
	public void cacheUnitTest() {

		LRUAlgoCacheImpl<Long, DataModel<Integer>> lru = new LRUAlgoCacheImpl<>(CAPACITY);
		DaoFileImpl<Integer> dao = new DaoFileImpl<>("outFile.txt");
		CacheUnit<Integer> cacheUnit = new CacheUnit(lru, dao);
		Long[] ids = new Long[CAPACITY];
		Integer[] nullArray = new Integer[CAPACITY];
		DataModel<Integer>[] dataModels = new DataModel[CAPACITY];
		DataModel myModel;

		for (int i = 0; i < CAPACITY; i++) {
			ids[i] = Long.valueOf(i);
			myModel = new DataModel(Long.valueOf(i), i);
			dataModels[i] = myModel;
			lru.putElement(Long.valueOf(i), myModel);
			dao.save(myModel);
			assertEquals(myModel.getContent(), dao.find(Long.valueOf(i)).getContent(),
					"if 'dao' contains the model, should return same content");

		}

		for (int i = 0; i < CAPACITY; i++) {
			lru.removeElement(dataModels[i].getDataModelId());
			dao.delete(dataModels[i]);
			// assertEquals(null, dao.find(Long.valueOf(i)),"after we deleted, 'find' should
			// return null");

		}

		cacheUnit.putDataModels(dataModels); // testing putDataModels method
		try {
			DataModel<Integer>[] tempModels = new DataModel[CAPACITY];
			tempModels = cacheUnit.getDataModels(ids);
			for (int i = 0; i < tempModels.length; i++) {
				assertEquals(tempModels[i], dataModels[i],
						"if put and get dataModels are working it should return the same models");
			}

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		cacheUnit.removeDataModels(ids);

		fillArrayWithNull(nullArray); // fills an array with null values

		try {

			assertArrayEquals(nullArray, cacheUnit.getDataModels(ids),
					"After we removed all models,getDataModels should return null");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}

	public void fillArrayWithNull(Integer[] arr) {
		for (int i = 0; i < CAPACITY; i++) {
			arr[i] = null;
		}

	}

}
