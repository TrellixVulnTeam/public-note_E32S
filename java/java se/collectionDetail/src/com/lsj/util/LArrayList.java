package com.lsj.util;

import java.util.Arrays;
import java.util.ConcurrentModificationException;

public class LArrayList<T> implements LList<T> {

	private static final int DEFAULT_CAPACITY = 10;
	Object[] items;
	private int size = 0;
	private int modCount = 0;
	
	public LArrayList() {
		items = new Object[DEFAULT_CAPACITY];
	}
	
	@Override
	public int size() {
		return size;
	}
	
	@Override
	public boolean contains(Object o) {
		if(o != null){
			for(Object item : items){
				if(item != null && item.equals(o)){
					return true;
				}
			}
		}else{
			for(Object item : items){
				if(item == null){
					return true;
				}
			}
		}
		
		return false;
	}
	
	@Override
	public boolean add(T nv) {
		ensureCapacity(size+1);
		items[size++] = nv;
		modCount++;
		return true;
	}
	
	@Override
	public boolean remove(T o) {
		if(o != null){
			for(int i=0; i<items.length; i++){
				if(o.equals(items[i])){
					remove(i);
					break;
				}
			}
		}else{
			for(int i=0; i<items.length; i++){
				if(items[i] == null){
					remove(i);
					break;
				}
			}
		}
		
		return true;
	}
	
	@Override
	public void add(int idx, T nv) {
		if(idx >= size || idx<0){
			throw new IndexOutOfBoundsException();
		}
		
		ensureCapacity(size+1);
		for(int i=size; i>idx; i--){
			items[i] = items[i-1];
		}
		items[idx] = nv;
		modCount++;
	}
	
	@Override
	public void remove(int idx) {
		checkRange(idx);
		for(int i=idx; i<size-1; i++){
			items[i] = items[i+1];
		}
		modCount++;
		items[size-1]=null;
		size-=1;
	}
	@Override
	public void set(int idx, T nv) {
		items[idx] = nv;
		modCount++;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public T get(int idx) {
		checkRange(idx);
		return (T)items[idx];
	}
	
	private void checkRange(int idx){
		if(idx >= items.length || idx < 0){
			throw new IndexOutOfBoundsException();
		}
	}
	
	private void ensureCapacity(int nextSize){
		if(nextSize>=items.length){
			items = Arrays.copyOf(items, items.length * 2);	
		}
	}
	
	
	@Override
	public LIterator<T> iterator() {
		return new ArrayListIterator();
	}
	
	private class ArrayListIterator implements LIterator<T>{
		private int expectedModCount = modCount;
		private int cursor = 0;
		private int ret = -1;
		
		@Override
		public boolean hasNext() {
			if(expectedModCount != modCount){
				throw new ConcurrentModificationException();
			}
			return (cursor<size);
		}

		@SuppressWarnings("unchecked")
		@Override
		public T next() {
			if(expectedModCount != modCount){
				throw new ConcurrentModificationException();
			}
			ret=cursor;
			return (T) items[cursor++];
		}
		
		@Override
		public void remove(){
			if(expectedModCount != modCount){
				throw new ConcurrentModificationException();
			}
			if(ret == -1){
				throw new IllegalStateException();
			}
			
			LArrayList.this.remove(ret);
			ret = -1;
		}
		
	}
	
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for(LIterator<T> it = iterator(); it.hasNext();){
			sb.append(it.next().toString()+", ");
		}
		return sb.toString();
	}

}
