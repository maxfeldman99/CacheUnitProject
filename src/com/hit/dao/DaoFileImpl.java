package com.hit.dao;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.LinkedHashMap;

import com.hit.dm.DataModel;

public class DaoFileImpl<T> implements IDao<Long, DataModel<T>> {

	private String filePath;
	private ObjectInputStream inputStream = null;
	private ObjectOutputStream outputStream = null;
	private LinkedHashMap<Long, DataModel<T>> hashMap; // to hold our data from file

	public DaoFileImpl(String filePath) {
		this.filePath = filePath;
		hashMap = new LinkedHashMap<>();
	}

	public DaoFileImpl(String filePath, int capacity) {
		this.filePath = filePath;
		hashMap = new LinkedHashMap<>(capacity);
	}

	@Override
	public void save(DataModel<T> t) { // this method will save our data inside a text file
		try {
			openInStream(); // first , we need to update our hashMap
			if (t != null) {
				hashMap.put(t.getDataModelId(), t);
				openOutStream();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		finally { // finally block to close my streams no matter what happened before
			closeStreamSafe();
		}

	}

	@Override
	public void delete(DataModel<T> t) throws IllegalArgumentException { // this method will delete our data from the
																			// text file
		openOutStream(); /// was changed since i tried to open instream first
		openInStream();
		try {
			if (hashMap.containsKey(t.getDataModelId()) && hashMap.get(t.getDataModelId()) != null) {
				openOutStream();
				DataModel<T> nullModel = new DataModel(t.getDataModelId(), null);
				hashMap.put(t.getDataModelId(), nullModel); // we set the current id content as null;

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeStreamSafe();
		}
	}

	@Override
	public DataModel<T> find(Long id) throws IllegalArgumentException { // this method will find our data located inside
																		// a text file

		if (id == null) {
			throw new IllegalArgumentException(); // making sure no null id was given;
		}

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
			e.printStackTrace();
		}
		try {
			if (outputStream != null)
				outputStream.flush();
			outputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
