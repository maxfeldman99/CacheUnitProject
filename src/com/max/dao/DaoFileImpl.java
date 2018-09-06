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

public class DaoFileImpl<T> implements IDao<Long, DataModel<T>> {

	String filePath;
	ObjectInputStream inputStream = null;
	ObjectOutputStream outputStream = null;
	LinkedHashMap<Long, DataModel<T>> hashMap; // to hold our data from file

	public DaoFileImpl(String filePath) {
		this.filePath = filePath;
		hashMap = new LinkedHashMap<>();
	}

	public DaoFileImpl(String filePath, int capacity) {
		this.filePath = filePath;
		hashMap = new LinkedHashMap<>(capacity);
	}

	@Override
	public void save(DataModel<T> entity) {
		try {
			openInStream(); // first , we need to update our hashMap

			if (entity != null) {
				hashMap.put(entity.getDataModelId(), entity);
				openOutStream();
			}

		} finally {
			closeStreamSafe();
		}

	}

	@Override
	public void delete(DataModel<T> entity) throws IllegalArgumentException {
		openInStream();
		try {
			if (hashMap.containsKey(entity.getDataModelId()) && hashMap.get(entity.getDataModelId()) != null) {
				openOutStream();
				DataModel<T> nullModel = new DataModel(entity.getDataModelId(), null);
				hashMap.put(entity.getDataModelId(), nullModel); // we set the current id content as null;

			}

		} finally {
			closeStreamSafe();
		}
	}

	@Override
	public DataModel<T> find(Long id) throws IllegalArgumentException {

		try {
			if ((hashMap.containsKey(id)) && (hashMap.get(id).getContent() != null)) {
				return hashMap.get(id);

			} else {
				return null;
			}

		} finally {

			try {
				outputStream = new ObjectOutputStream(new FileOutputStream(filePath, false));
				outputStream.writeObject(hashMap);
			} catch (Exception e) {
				e.printStackTrace();
			}

			closeStreamSafe();
		}

	}

	private void openInStream() { // to read from our file
		try {
			inputStream = new ObjectInputStream(new FileInputStream(filePath));
			hashMap = (LinkedHashMap<Long, DataModel<T>>) inputStream.readObject(); // hashMap receives my data

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void openOutStream() { // to write to our file
		try {
			outputStream = new ObjectOutputStream(new FileOutputStream(filePath, false));
			outputStream.writeObject(hashMap); // hashMap receives my data

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void closeStreamSafe() { // each time we use in/out streams we will eventually close the streams inside
										// finally
		try {
			if (inputStream != null)
				inputStream.close();
		} catch (IOException e) {
		}
		try {
			if (outputStream != null)
				outputStream.flush();
			outputStream.close();
		} catch (IOException e) {
		}
	}

}
