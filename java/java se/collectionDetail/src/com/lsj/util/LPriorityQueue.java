package com.lsj.util;

import java.util.Arrays;
import java.util.NoSuchElementException;

public class LPriorityQueue<T extends Comparable<? super T>> implements LQueue<T>{

	private T[] items;
	private int size;		//
	private int tail=1;
	
	@Override
	public int size() {
		return size;
	}

	@Override
	public boolean isEmpty() {
		return size == 0;
	}

	@Override
	public boolean add(T element) {
		ensuerCapacity(tail+1);
		return false;
	}

	@Override
	public T poll() {
		if(tail == 1){
			return null;
		}
		
		T temp = items[1];
		fitDown();
		return temp;
	}

	@Override
	public T remove() {
		if(tail == 1){
			throw new NoSuchElementException();
		}
		
		return null;
	}
	
	private void ensuerCapacity(int nextTail){
		if(nextTail == items.length){
			items = Arrays.copyOf(items, items.length<<1);
		}
	}
	
	private void fitUp(T element){
		
	}

	private void fitDown(){
		T temp = items[tail-1];
		
		int index=1;
		for(; index * 2 <= tail-1; ){
			int leftIndex = index*2;
			int rightIndex = index*2 + 1;
			int result = items[leftIndex].compareTo(items[rightIndex]);
			if(rightIndex == tail-1){
				
			}
			
			if( result < 0 ){
				items[index] = items[leftIndex];
				index = leftIndex;
			}else if( result > 0 ){
				items[index] = items[rightIndex];
				index = rightIndex;
			}else{
				items[index] = items[leftIndex];
				index = leftIndex;
			}
		}
		
		items[index] = temp;
	}
}
