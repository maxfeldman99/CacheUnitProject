package com.max.dao;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.LinkedHashMap;

import com.max.dm.DataModel;

public class DaoFileImpl<T> extends java.lang.Object implements IDao<Long, DataModel<T>> {

	String filePath;
	ObjectInputStream inputStream = null;
	ObjectOutputStream outputStream = null;
	LinkedHashMap<Long, DataModel<T>> hashMap; // to hold our data from file
	

	public DaoFileImpl(String filePath) {
		this.filePath = filePath; //"DataSource.txt"
		hashMap = new LinkedHashMap<>();
	}
	
	public DaoFileImpl(){
        filePath = "outFile.txt";
        
    }
	

	@Override
	public void save(DataModel<T> entity) {
		try {
			openInStream(); // first , we need to update our hashMap
			
			if(entity!=null) {
			hashMap.put(entity.getDataModelId(), entity);
			openOutStream();
			}
			
		} finally {
			closeStreamSafe();
		}
		
		//write on my database
		//open output
		//write
		//close
		
		

	}

	@Override
	public void delete(DataModel<T> entity) throws IllegalArgumentException {
		
		try {
			if(hashMap.containsKey(entity.getDataModelId())) {
				openOutStream();
				hashMap.remove(entity.getDataModelId(), entity); // only makes null , its don
			}
		
		} finally {
			closeStreamSafe();
		}
		//open output
		// delete from map
		//update  stream
		//close
	}

	@Override
	public DataModel<T> find(Long id) throws IllegalArgumentException {
		
		try {
			openInStream();
			if(hashMap.containsKey(id)) {
				return hashMap.get(id);
				
			}else {
				return null;
			}
		
		} finally {
			closeStreamSafe();
		}
		
	}

	private void openInStream() { // to read from our file
		try {
			inputStream = new ObjectInputStream(new FileInputStream(filePath));
			hashMap = (LinkedHashMap<Long, DataModel<T>>) inputStream.readObject(); //hashMap receives my data
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	private void openOutStream() { //  to write to our file
		try {
			outputStream = new ObjectOutputStream(new FileOutputStream(filePath, false));
			outputStream.writeObject(hashMap); //hashMap receives my data
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void closeStreamSafe() { // each time we use in/out streams we will eventually close the streams inside finally
		try {
			if (inputStream != null)
				inputStream.close();
		} catch (IOException e) {
		}
		try {
			if (outputStream != null)
				outputStream.close();
		} catch (IOException e) {
		}
	}
	
		
		
	}


