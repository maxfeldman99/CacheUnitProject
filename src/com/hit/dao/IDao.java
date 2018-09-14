package com.hit.dao;

import java.lang.IllegalArgumentException;
import java.io.Serializable;

public interface IDao<ID extends Serializable, T> {

	T find(ID id) throws IllegalArgumentException;

	void delete(T entity) throws IllegalArgumentException;

	void save(T entity);
}
