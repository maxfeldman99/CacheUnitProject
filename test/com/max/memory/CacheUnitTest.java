package com.max.memory;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import org.junit.jupiter.api.Test;


import com.max.algorithm.LRUAlgoCacheImpl;
import com.max.dao.DaoFileImpl;
import com.max.dm.DataModel;



public class CacheUnitTest {
	
	private static final int CAPACITY = 10;
	public Integer randomNum = (int) (Math.random() * CAPACITY);
	
	
	
	@Test
	public void cacheUnitTest() {
		
		LRUAlgoCacheImpl<Long,DataModel<Integer>> lru = new LRUAlgoCacheImpl<>(CAPACITY);
		DaoFileImpl<Integer> dao = new DaoFileImpl<>("outFile.txt");
		CacheUnit<Integer> cacheUnit = new CacheUnit(lru, dao);
		Long[] ids = new Long[CAPACITY];
		Integer[] nullArray = null;
		DataModel<Integer>[] dataModels = new DataModel[CAPACITY];
		DataModel a;
		long id = 1;
		
		
		
		for(int i=0 ; i <CAPACITY ; i++) {
			ids[i] = Long.valueOf(i);
			a = new DataModel(Long.valueOf(i),i);
			dataModels[i] = a;
			lru.putElement(Long.valueOf(i),a);
			dao.save(a);
			System.out.println("Saving item: " +a);
			
		}
		
		for (int i = 0; i < CAPACITY; i++) {
			System.out.println("finding item: "+dao.find(Long.valueOf(i)));
			
		}
		
		
		for (int i = 0; i < CAPACITY; i++) {
			lru.removeElement(Long.valueOf(i));
			dao.delete(new DataModel(Long.valueOf(i),i));
			System.out.println("deleting data model: " + i);
		}
		
		
		for (int i = 0; i < CAPACITY; i++) {
			System.out.println(dao.find(Long.valueOf(i)));
		}
		
		
		
		cacheUnit.putDataModels(dataModels);
		
		
		try {
			cacheUnit.getDataModels(ids);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		cacheUnit.removeDataModels(ids);
		
		fillArrayWithNull(nullArray);
		
		try {
			
			assertArrayEquals(nullArray,cacheUnit.getDataModels(ids),"After we removed all models,getDataModels should return null");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
//		for (int i = 0; i < dataModels.length; i++) {
//			dataModels[i].setDataModelId(Long.valueOf(id++));
//			dataModels[i].setContent(randomNum++);
//			
//			System.out.println(ids[i]);
//		}
//		
//		cacheUnit.putDataModels(dataModels);
		
		
//		try {
//			dms = cacheUnit.getDataModels(ids);
//		} catch (ClassNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		
		
		
	}
	
	public Integer[] fillArrayWithNull(Integer[] arr) {
		for (int i = 0; i < CAPACITY; i++) {
			arr[i] = null;
		}
		return arr;
	}
	

}
