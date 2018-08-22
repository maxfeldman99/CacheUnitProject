package com.max.memory;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.max.algorithm.LRUAlgoCacheImpl;
import com.max.dao.DaoFileImpl;
import com.max.dm.DataModel;

@DisplayName("CacheUnit Test")
class CacheUnitTest {

	private static final int CAPACITY = 10;
	// private Integer randomNum = (int) (Math.random() * CAPACITY);
	public static LRUAlgoCacheImpl<Long, DataModel<Integer>> lru = new LRUAlgoCacheImpl<>(CAPACITY);
	public static DaoFileImpl<Integer> dao = new DaoFileImpl<>("outFile.txt");
	public static CacheUnit<Integer> cacheUnit = new CacheUnit(lru, dao);
	public static Long[] ids = new Long[CAPACITY];
	public static Integer[] nullArray = new Integer[CAPACITY];
	public static DataModel<Integer>[] dataModels = new DataModel[CAPACITY];
	public static DataModel<Integer> myModel;

	@Test
	@BeforeAll
	static void setUpWithData() {
		for (int i = 0; i < CAPACITY; i++) {
			ids[i] = Long.valueOf(i);
			myModel = new DataModel(Long.valueOf(i), i);
			dataModels[i] = myModel;
			lru.putElement(Long.valueOf(i), myModel);
			dao.save(myModel);
			
//			if(dataModels[i].equals(dao.find(dataModels[i].getDataModelId()))) {
//				System.out.println("YESSSSSSSSSS");
//			}
			
			assertEquals(dataModels[i].getDataModelId(), dao.find(dataModels[i].getDataModelId()).getDataModelId());
		}
	}
	
	@Test
	@AfterAll
	static void afterAll() {
		for (int i = 0; i < CAPACITY; i++) {
			lru.removeElement(dataModels[i].getDataModelId());
			dao.delete(dataModels[i]);
			//assertEquals(null, dao.find(dataModels[i].getDataModelId()));
			

		}
	}

	@Nested
	@DisplayName("Testing the Cache unit")
	class TestingCacheUnit {

		@Test
		@DisplayName("Example test for method A")
		void sampleTestForMethodA() {
			cacheUnit.putDataModels(dataModels); // testing putDataModels method
			try {
				DataModel<Integer>[] tempModels = new DataModel[CAPACITY];
				tempModels = cacheUnit.getDataModels(ids);
				for (int i = 0; i < tempModels.length; i++) {
					assertEquals(tempModels[i], dataModels[i],
							"if put and get dataModels are working it should return the same models");
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();

			}
		}

		@Nested
		@DisplayName("When Models inserted")
		class WhenModelsInsetred {

			@Test
			@DisplayName("Deleting models using cacheUnit")
			void deleteModels() {
				cacheUnit.removeDataModels(ids);
				fillArrayWithNull(nullArray); // fills an array with null values
				try {
					assertArrayEquals(nullArray, cacheUnit.getDataModels(ids),
							"After we removed all models,getDataModels should return null");
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();

				}
			}
		}
	}

	@Nested
	@DisplayName("Tests for the method A")
	class Optional {

	}

	public void fillArrayWithNull(Integer[] arr) {
		for (int i = 0; i < CAPACITY; i++) {
			arr[i] = null;
		}

	}
}
