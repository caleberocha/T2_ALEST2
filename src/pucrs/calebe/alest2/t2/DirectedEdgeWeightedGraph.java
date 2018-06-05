package pucrs.calebe.alest2.t2;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Grafo direcionado com pesos nas arestas.
 * @author calebe
 */
public class DirectedEdgeWeightedGraph {
	
	private class Vertex {
		private String name;
		private int cost;
		private int mark;
		private BigInteger totalCost;
		
		public Vertex(String name, int cost) {
			this.name = name;
			this.cost = cost;
			this.mark = 0;
			this.totalCost = BigInteger.ZERO;
		}
		
		@Override
		public String toString() {
			return name + " " + cost;
		}
	}
	
	private class Edge {
		private int vertex1;
		private int vertex2;
		private int weight;
		
		public Edge(int vertex1, int vertex2, int weight) {
			this.vertex1 = vertex1;
			this.vertex2 = vertex2;
			this.weight = weight;
		}
		
		@Override
		public String toString() {
			return String.format("[%d \u2192 %d: %d]", this.vertex1, this.vertex2, this.weight);
		}
	}
	
	private List<Vertex> vertices;
	private List<LinkedList<Edge>> adjList;
	private List<Integer> firstVertex;
	
	public DirectedEdgeWeightedGraph(int size) {
		vertices = new ArrayList<>(size);
		adjList = new ArrayList<>(size);
		firstVertex = new ArrayList<>(size);
		for(int i = 0; i < size; i++)
			adjList.add(new LinkedList<>());
	}
	
	public void addEdge(String name1, String name2, int weight) {
		int n1 = vertexIndexOf(name1);
		if(n1 == -1)
			throw new NoSuchElementException("Projeto " + name1 + " não encontrado.");
		
		int n2 = vertexIndexOf(name2);
		if(n2 == -1)
			throw new NoSuchElementException("Projeto " + name2 + " não encontrado.");
		
		Edge e = new Edge(n1, n2, weight);
		adjList.get(n1).add(e);
		firstVertex.remove((Integer) n2);
	}
	
	public void addVertex(String name, int cost) {
		addVertex(new Vertex(name, cost));
	}
	
	public void addVertex(Vertex element) {
		vertices.add(element);
		firstVertex.add(vertices.indexOf(element));
	}
	
	public List<Vertex> getVertices() {
		return vertices;
	}
	
	private int vertexIndexOf(String name) {
		for(int i = 0; i < vertices.size(); i++) {
			if(vertices.get(i).name.equals(name))
				return i;
		}
		return -1;
	}
	
	private BigInteger getTotalCost(int index) {
		Vertex v = vertices.get(index);
		if(v.totalCost.equals(BigInteger.ZERO)) {
			BigInteger partialSum = BigInteger.valueOf(v.cost);
			for (Edge e : adjList.get(index)) {
				BigInteger weight = BigInteger.valueOf(e.weight);
				weight = weight.multiply(getTotalCost(e.vertex2));
				partialSum = partialSum.add(weight);
			}
			v.totalCost = partialSum;
			return partialSum;
		}
		return v.totalCost;
	}
	
	public BigInteger getTotalCost(Vertex elem) {
		int index = vertices.indexOf(elem);
		if(index == -1)
			return BigInteger.ZERO;
		return getTotalCost(index);	
	}
	
	public BigInteger getTotalCost(String elem) {
		int index = vertexIndexOf(elem);
		if(index == -1)
			return BigInteger.ZERO;
		return getTotalCost(index);	
	}
	
	public BigInteger getTotalCost() {
		if(firstVertex.size() > 1)
			throw new IllegalArgumentException("Há mais de um vértice inicial!");
		
		return getTotalCost(firstVertex.get(0));
	}
	
	public boolean containsCycle() {
		for(Vertex v : vertices)
			v.mark = 0;
		
		for(Vertex v : vertices)
			if(v.mark == 0 && visit(v))
				return true;
		
		return false;
	}
	
	private boolean visit(Vertex v) {
		v.mark = 1;		
		for(Edge e : adjList.get(vertices.indexOf(v))) {
			Vertex u = vertices.get(e.vertex2);
			if(u.mark == 1 || u.mark == 0 && visit(u))
				return true;
		}
		v.mark = 2;
		return false;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("First: " + firstVertex).append(System.lineSeparator());
		for(int i = 0; i < adjList.size(); i++) {
			sb.append(i).append(" = ").append(vertices.get(i)).append(": ");
			for(Edge e : adjList.get(i))
				sb.append("[").append(vertices.get(e.vertex2)).append("]").append("=").append(e.weight).append(", ");
				//sb.append(e).append(", ");
			sb.append(System.lineSeparator());
		}
		return sb.toString();
	}
	
	public String toGraphViz() {
		StringBuilder sb = new StringBuilder();
		sb.append("digraph G {").append(System.lineSeparator());
		for(int i = 0; i < adjList.size(); i++) {
			for(Edge e : adjList.get(i)) {
				sb.append("    \"")
				.append(vertices.get(i)).append("\"")
				.append(" -> \"")
				.append(vertices.get(e.vertex2)).append("\"")
				.append(System.lineSeparator());
			}
		}
		sb.append("}").append(System.lineSeparator());
		
		return sb.toString();
	}
}
