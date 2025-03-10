package com.lsj.util;

import java.util.ConcurrentModificationException;

public class LLinkedList<T> implements LList<T> {

	
	private int size = 0;
	private int modCount = 0;
	Node<T> head;
	Node<T> tail;
	
	
	@Override
	public int size() {
		return size;
	}

	@Override
	public boolean contains(Object o) {
		return false;
	}

	@Override
	public boolean add(T nv) {
		Node<T> newNode = new Node(tail, nv, null);		
		if(tail == null){
			head = newNode;
		}else{
			tail.next = newNode;
		}
		tail = newNode;
		size++;
		modCount++;
		return true;
	}

	@Override
	public boolean remove(T o) {
		for(Node<T> curr=head; curr!=null; curr=curr.next){
			if(o == null){
				if(curr.element == null){
					curr.prev.next = curr.next;
					curr.next.prev = curr.prev;
					size--;
					modCount++;
					return true;
				}
			}else{
				if(o.equals(curr.element)){
					curr.prev.next = curr.next;
					curr.next.prev = curr.prev;
					size--;
					modCount++;
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public void add(int idx, T nv) {
		if(idx > size || idx < 0){
			throw new IndexOutOfBoundsException();
		}
		
		Node<T> newNode = new Node<T>(nv);
		Node<T> curr = getNode(idx);
		if(idx == 0){
			newNode.next = head;
			head = newNode;
		}else if(idx == size){
			tail.next = curr;
			curr.prev = tail;
			tail = curr;
		}else{
			newNode.next = curr;
			curr.prev.next = newNode;
		}
		size++;
		modCount++;
	}

	@Override
	public void remove(int idx) {
		if(idx >= size || idx < 0){
			throw new IndexOutOfBoundsException();
		}
		
		Node<T> curr = getNode(idx);
		if(idx == 0){
			head = head.next;
		}else if(idx == size-1){
			tail = tail.prev;
		}else{
			curr.prev.next = curr.next;
			curr.next.prev = curr.prev;
			curr.element = null;
			curr = null;	
		}
		size--;
		modCount++;
	}

	@Override
	public void set(int idx, T nv) {
		if(idx >= size || idx < 0){
			throw new IndexOutOfBoundsException();
		}
		getNode(idx).element = nv;
	}

	@Override
	public T get(int idx) {
		if(idx >= size || idx < 0){
			throw new IndexOutOfBoundsException();
		}
		
		return getNode(idx).element;
	}
	
	private Node<T> getNode(int idx){
		Node<T> curr;
		if(idx < size/2){
			curr = head;
			for(int i=0; i<idx; i++){
				curr = curr.next;
			}
		}else{
			curr = tail;
			for(int i=size-1; i>=idx-1; i--){
				curr = curr.prev;
			}
		}
		return curr;
	}
	
	@Override
	public LIterator<T> iterator() {
		return new Ltr();
	}
	
	private class Ltr implements LIterator<T>{
		private int expectedModCount = modCount;
		Node<T> curr = head;
		
		@Override
		public boolean hasNext() {
			if(expectedModCount != modCount){
				throw new ConcurrentModificationException();
			}
			return (curr != null);
		}

		@Override
		public T next() {
			if(expectedModCount != modCount){
				throw new ConcurrentModificationException();
			}
			T currValue = curr.element;
			curr = curr.next;
			return currValue;
		}
	}
	
	static private class Node<T>{
		private T element;
		private Node<T> next;
		private Node<T> prev;
		
		Node(T ele){
			this(null, ele, null);
		}
		
		Node(Node<T> pre, T ele, Node<T> nxt){
			element = ele;
			prev = pre;
			next = nxt;
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
