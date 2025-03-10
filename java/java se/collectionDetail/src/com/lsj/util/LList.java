package com.lsj.util;


public interface LList<T> extends LCollection<T>{
	
	void add(int idx, T nv);
	
	void remove(int idx);
	
	void set(int idx, T nv);
	
	T get(int idx);
}
