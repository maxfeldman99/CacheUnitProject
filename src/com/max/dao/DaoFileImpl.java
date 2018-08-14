package com.max.dao;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.max.dm.DataModel;

public class DaoFileImpl<T> extends java.lang.Object implements IDao<Long,DataModel<T>> {
	
	
	 String filePath;
	 ObjectInputStream inputStream = null;
	 ObjectOutputStream outputStream = null;
	
	
	DaoFileImpl(String filePath) {
		this.filePath = filePath;
	}
	
	
	@Override
	public void save(DataModel<T> entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(DataModel<T> entity) throws IllegalArgumentException {
	
	}

	@Override
	public DataModel<T> find(Long id) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		return null;
	}
	
	private void openInputStream() {
		
	}
	
	private void executeTest() throws FileNotFoundException, IOException  { 
		
			
			try {

			    inputStream = new ObjectInputStream(new FileInputStream(filePath));
			    outputStream = new ObjectOutputStream(new FileOutputStream(filePath,false));

			    /////....

			} finally {

			    try { if (inputStream != null) inputStream.close(); } catch(IOException e) {}
			    try { if (outputStream != null) outputStream.close(); } catch(IOException e) {}

			}
	}
		
	


	
}

	
