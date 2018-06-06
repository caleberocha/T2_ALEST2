package pucrs.calebe.alest2.t2;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MyHashTable<K, V> {
	private int chainsCount;
	private int size;
	private Node[] lists;
	private int opCount;
	
	private static final int INITIAL_CAPACITY = 16;
	
	private static class Node {
		private Object key;
		private Object value;
		private Node next;
		
		private Node(Object key, Object value) {
			this(key, value, null);
		}
		
		private Node(Object key, Object value, Node next) {
			this.key = key;
			this.value = value;
			this.next = next;
		}
	}
	
	public MyHashTable(int size) {
		chainsCount = size / 3;
		lists = new Node[chainsCount];
		this.size = 0;
		this.opCount = 0;
	}
	
	public MyHashTable() {
		this(INITIAL_CAPACITY);
	}
	
	private int hash(K key) {
		return (key.hashCode() & 0x7fffffff) % chainsCount;
	}
	
	@SuppressWarnings("unchecked")
	public V get(K key) {
		int i = hash(key);
		Node n = lists[i];
		while(n != null) {
			opCount++;
			if(key.equals(n.key))
				return (V) n.value;
			n = n.next;
		}
		return null;
	}
	
	public void put(K key, V value) {
		int i = hash(key);
		Node n = lists[i];
		while(n != null) {
			opCount++;
			if(key.equals(n.key)) {
				n.value = value;
				return;
			}
			n = n.next;
		}
		lists[i] = new Node(key, value, lists[i]);
		size++;
	}
	
	public void putIfAbsent(K key, V value) {
		int i = hash(key);
		Node n = lists[i];
		while(n != null) {
			opCount++;
			if(key.equals(n.key))
				return;
			n = n.next;
		}
		lists[i] = new Node(key, value, lists[i]);
		size++;
	}
	
	public boolean remove(K key) {
		int i = hash(key);
		Node n = lists[i];
		Node prev = null;
		while(n != null) {
			opCount++;
			if(key.equals(n.key)) {
				if(prev != null)
					prev.next = n.next;
				else
					lists[i] = n.next;
				size--;
				return true;
			}
			prev = n;
			n = n.next;
		}
		return false;
	}
	
	public int size() {
		return size;
	}
	
	public boolean containsKey(K key) {
		for(Node n : lists) {
			while(n != null) {
				opCount++;
				if(key.equals(n.key))
					return true;
				n = n.next;
			}	
		}
		return false;
	}
	
	@SuppressWarnings("unchecked")
	public Set<K> keySet() {
		Set<K> keys = new HashSet<>(size);
		for(Node n : lists) {
			while(n != null) {
				opCount++;
				keys.add((K) n.key);
				n = n.next;
			}	
		}
		return keys;
	}
	
	@SuppressWarnings("unchecked")
	public List<V> values() {
		List<V> val = new ArrayList<>(size);
		for(Node n : lists) {
			while(n != null) {
				opCount++;
				val.add((V) n.value);
				n = n.next;
			}	
		}
		return val;
	}
	
	public int getOpCount() {
		return opCount;
	}
	
}
