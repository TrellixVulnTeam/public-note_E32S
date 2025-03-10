package com.lsj.util;

import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;

public class BinarySearchTreeMap<K extends Comparable<? super K>, V> implements LMap<K, V> {

	Node<K, V> root;
	private int size = 0;
	private int modCount = 0;
	
	@Override
	public int size() {
		return size;
	}

	@Override
	public boolean isEmpty() {
		return (size == 0);
	}

	@Override
	public boolean containsKey(Object key) {
		return getNode((K) key, root) != null;
	}

	@Override
	public boolean containsValue(Object value) {
		return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public V get(Object key) {
		return getNode((K) key, root).val;
	}

	@Override
	public V put(K key, V value) {
		root = put(key, value, root);
		size++;
		modCount++;
		return null;
	}
	
	@Override
	public V remove(Object key) {
		remove((K) key, root);
		size--;
		modCount++;
		return null;
	}

	@Override
	public void clear() {
		clear(root);
		root = null;
		size = 0;
		modCount++;
	}
	
	@Override
	public LCollection<V> values() {
		return null;
	}
	
	/**
	 * 
	 * 找到指定key的node
	 * */
	private Node<K, V> getNode(K key, Node<K, V> node){
		if(node == null){
			return null;
		}
		
		int result = node.key.compareTo(key);
		if(result < 0){
			return getNode(key, node.lft);
		}else if(result > 0){
			return getNode(key, node.rgt);
		}else{
			return node;
		}
	}
	
	/**
	 * 
	 * 插入一个kv值到节点node的树中中，返回新的根节点
	 * */
	private Node<K, V> put(K key, V value, Node<K, V> node){
		if(node == null){
			return new Node<K, V>(key, value);
		}
		
		int result = node.key.compareTo(key);
		if(result < 0){
			node.lft = put(key, value, node.lft);
		}else if(result > 0){
			node.rgt = put(key, value, node.rgt);
		}else{
			node.val = value;
		}
		
		return node;
	}
	
	private void clear(Node<K, V> node){
		if(node == null){
			return ;
		}
		
		node.val = null;
		
		clear(node.lft);
		node.lft = null;
		
		clear(node.rgt);
		node.rgt = null;
	}
	
	private Node<K, V> remove(K key, Node<K, V> node){
		if(node == null){
			return node;
		}
		
		int result = node.key.compareTo(key);
		if(result < 0){
			node.lft = remove(key, node.lft);
		}else if(result > 0){
			node.rgt = remove(key, node.rgt);
		}else if(node.lft != null && node.rgt != null){
			Node<K, V> rgtMinNode = findMinNode(node.rgt);
			node.key = rgtMinNode.key;
			node.val = rgtMinNode.val;
			remove(node.key, node.rgt);
		}else{
			node = (node.rgt == null ? node.lft : node.rgt);
		}
		
		return node;
	}
	
	private Node<K, V> findMinNode(Node<K, V> node){
		while(node!=null && node.lft == null){
			node = node.lft;
		}
		return node;
	}
	
	static private class Node<K, V>{
		K key;
		V val;
		Node<K, V> lft;
		Node<K, V> rgt;
		
		Node(K key, V val){
			this(key, val, null, null);
		}
		
		Node(K key, V val, Node<K, V> lft, Node<K, V> rgt){
			this.key = key;
			this.val = val;
			this.lft = lft;
			this.rgt = rgt;
		}
	}

	@Override
	public LSet<LEntry<K, V>> entrySet() {
		return new EntrySet();
	}
	
	private static class EntrySet implements LSet{
		@Override
		public int size() {
			return 0;
		}

		@Override
		public boolean contains(Object o) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean add(Object nv) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean remove(Object o) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public LIterator iterator() {
			// TODO Auto-generated method stub
			return null;
		}
	}
	
	private class Itr implements LIterator<LEntry<K, V>>{
		int curr = 0;
		int expectedModCount = 0;
		
		@Override
		public boolean hasNext() {
			if(expectedModCount != modCount){
				throw new ConcurrentModificationException();
			}
			return curr<=size;
		}

		@Override
		public LEntry<K, V> next() {
			if(expectedModCount != modCount){
				throw new ConcurrentModificationException();
			}
			if(size == 0){
				throw new NoSuchElementException();
			}
			
			return null;
		}
		
	}
	
	private static class KVEntry<K, V> implements LEntry<K, V>{
		K key;
		V value;
		
		KVEntry(K k, V v){
			key = k;
			value = v;
		}
		
		
		@Override
		public K getKey() {
			return key;
		}

		@Override
		public V getValue() {
			return value;
		}
		
	}
	
}
