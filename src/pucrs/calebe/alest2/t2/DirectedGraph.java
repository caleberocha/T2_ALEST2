package pucrs.calebe.alest2.t2;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Grafo direcionado com elementos nas arestas.
 * @author calebe
 *
 * @param <E> Elemento dos nodos
 * @param <EE> Elemento das arestas
 */
public class DirectedGraph<E, EE> {
	
	private class Node {
		private E element;
		
		protected Node(E element) {
			this.element = element;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + ((element == null) ? 0 : element.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Node other = (Node) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (element == null) {
				if (other.element != null)
					return false;
			} else if (!element.equals(other.element))
				return false;
			return true;
		}

		private DirectedGraph<E, EE> getOuterType() {
			return DirectedGraph.this;
		}
	}
	
	private class Edge<EEE> {
		private EEE element;
		
		protected Edge(EEE element) {
			this.element = element;
		}
	}
	
	Map<Node, Integer> nodeList;
	int nodeListSize;
	Edge<EE> adjMatrix[][];
	
	@SuppressWarnings("unchecked")
	public DirectedGraph(int size) {
		nodeList = new HashMap<>(size);
		nodeListSize = 0;
		adjMatrix = new Edge[size][size];
	}
	
	public boolean addNode(E element) {
		if(nodeList.containsKey(element))
			return false;
		
		if(nodeList.put(new Node(element), nodeListSize) == nodeListSize) {
			nodeListSize++;
			return true;
		}
		return false;
	}
	
	/*private Node getNode(E element) {
		Node n = new Node(element);
		if(nodeList.containsKey(n))
			return n;
		return null;
	}*/
	
	private Node getNode(Integer index) {
		if(!nodeList.containsValue(index))
			return null;
		return nodeList.entrySet().stream().filter(e -> e.getValue() == index).findFirst().get().getKey();
	}
	
	private Integer indexOf(E element) {
		return nodeList.get(new Node(element));
	}
	
	public boolean addEdge(E from, E to, EE element) {
		Integer nFrom = indexOf(from);
		if(nFrom == null)
			return false;
		
		Integer nTo = indexOf(to);
		if(nTo == null)
			return false;
		
		adjMatrix[nFrom][nTo] = new Edge<EE>(element);
		return true;
	}
	
	public EE getEdge(E from, E to) {
		Integer nFrom = indexOf(from);
		if(nFrom == null)
			return null;
		
		Integer nTo = indexOf(to);
		if(nTo == null)
			return null;
		
		return adjMatrix[nFrom][nTo].element;
	}
	
	public Set<E> getNeighbors(E element) {
		Integer nFrom = indexOf(element);
		if(nFrom == null)
			return null;
		
		Set<E> neighbors = new HashSet<>();
		for(int i = 0; i < adjMatrix[nFrom].length; i++)
			if(adjMatrix[nFrom][i] != null)
				neighbors.add(getNode(i).element);
		
		return neighbors;
	}
}
