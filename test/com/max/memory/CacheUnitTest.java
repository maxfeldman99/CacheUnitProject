package com.max.memory;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import com.max.algorithm.IAlgoCache;
import com.max.algorithm.LRUAlgoCacheImpl;
import com.max.dao.DaoFileImpl;
import com.max.dm.DataModel;

@DisplayName("CacheUnit Test")
class CacheUnitTest {

	private static final int CAPACITY = 10;
	// private Integer randomNum = (int) (Math.random() * CAPACITY);
	public static IAlgoCache<Long, DataModel<Integer>> lru = new LRUAlgoCacheImpl<>(CAPACITY/2);
	public static DaoFileImpl<Integer> dao = new DaoFileImpl<>("DataSource.txt",CAPACITY);
	public static CacheUnit<Integer> cacheUnit = new CacheUnit(lru);
	public static Long[] ids = new Long[CAPACITY];
	public static Integer[] nullArray = new Integer[CAPACITY];
	public static DataModel<Integer>[] dataModels = new DataModel[CAPACITY];
	public static DataModel<Integer> myModel = null;
	

	@Test
	@BeforeAll
	static void setUpWithData() {
		for (int i = 0; i < CAPACITY; i++) {
			ids[i] = Long.valueOf(i);
			myModel = new DataModel<Integer>(Long.valueOf(i), i);
			dataModels[i] = myModel;
			lru.putElement(Long.valueOf(i), myModel);
			dao.save(myModel);
			assertEquals(dataModels[i].getDataModelId(), dao.find(dataModels[i].getDataModelId()).getDataModelId());
		}
	}

	@Test
	@AfterAll
	static void afterAll() {
		for (int i = 0; i < CAPACITY; i++) {
			lru.removeElement(dataModels[i].getDataModelId());
			dao.delete(dataModels[i]);
			assertEquals(null, dao.find(dataModels[i].getDataModelId()));
			

		}
	}

	@Nested
	@DisplayName("Testing the Cache unit")
	class TestingCacheUnit {

		@Test
		@DisplayName("Testing insertion")
		void InsertionTest() throws ClassNotFoundException, IOException {
			cacheUnit.putDataModels(dataModels); // testing putDataModels method

			DataModel<Integer>[] tempModels = new DataModel[CAPACITY];
			tempModels = cacheUnit.getDataModels(ids);
			for (int i = 0; i < tempModels.length; i++) {
				assertEquals(String.valueOf(tempModels[i]),String.valueOf(dataModels[i]),
						"if put and get dataModels are working it should return the same models");
			}
		}

		@Nested
		@DisplayName("When Models inserted")
		class WhenModelsInsetred {

			@Test
			@DisplayName("Deleting models using cacheUnit")
			void deleteModelsTest() {
				cacheUnit.removeDataModels(ids);
				for (int i = 0; i < ids.length; i++) {
					assertEquals(null, lru.getElement(ids[i]),
							"After we removed all models,getDataModels should return null");
				}

			}
		}
	}

	@Nested
	@DisplayName("Tests for Dao")

	class DaoTest {

		@Test
		@DisplayName("Test for Dao")
		void testingDao() {
			DaoFileImpl<Integer> dao = new DaoFileImpl<>("DataSource.txt", CAPACITY);
			for (int i = 0; i < dataModels.length; i++) {
				dao.save(dataModels[i]);
				myModel = dao.find(dataModels[i].getDataModelId());
				assertEquals(dataModels[i].getDataModelId(), myModel.getDataModelId(),
						"checking if model was inserted");
			}
		}

		@Test
		@DisplayName("Test for Dao delete")
		void testingDaoDelete() {
			for (int i = 0; i < dataModels.length; i++) {
				dao.delete(dataModels[i]);
				myModel = dao.find(dataModels[i].getDataModelId());
				assertEquals(null, myModel,"checking if model was removed");
			}
		}

	}

}
