package com.max.dao;

import java.lang.IllegalArgumentException;
import java.io.Serializable;

public interface IDao<ID extends Serializable,T> {
	
	void save(T entity);
	void delete(T entity) throws IllegalArgumentException;
	T find(ID id) throws IllegalArgumentException;
}
