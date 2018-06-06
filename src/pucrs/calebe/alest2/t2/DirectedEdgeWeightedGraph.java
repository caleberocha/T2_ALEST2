package pucrs.calebe.alest2.t2;

import java.math.BigInteger;
import java.util.*;

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
		
		Vertex(String name, int cost) {
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
		private Vertex vertex1;
		private Vertex vertex2;
		private int weight;
		
		Edge(Vertex vertex1, Vertex vertex2, int weight) {
			this.vertex1 = vertex1;
			this.vertex2 = vertex2;
			this.weight = weight;
		}
		
		@Override
		public String toString() {
			return String.format("[%s \u2192 %s: %d]", this.vertex1, this.vertex2, this.weight);
		}
	}
	
	private Map<String, Vertex> vertices;
	private Map<Vertex, LinkedList<Edge>> adjList;
	private Map<String, Vertex> firstVertex;
	private int opAddVertexCount;
	private int opAddEdgeCount;
	private int opCalculateCostCount;

	public DirectedEdgeWeightedGraph(int size) {
		vertices = new HashMap<>();
		adjList = new HashMap<>();
		firstVertex = new HashMap<>();

		opAddEdgeCount = 0;
		opAddVertexCount = 0;
		opCalculateCostCount = 0;
	}
	
	public void addEdge(String name1, String name2, int weight) {
		Vertex v1 = vertices.get(name1);
		if(v1 == null)
			throw new NoSuchElementException("Projeto " + name1 + " não encontrado.");
		
		Vertex v2 = vertices.get(name2);
		if(v2 == null)
			throw new NoSuchElementException("Projeto " + name2 + " não encontrado.");
		
		addEdge(v1, v2, weight);
	}

	private void addEdge(Vertex v1, Vertex v2, int weight) {
		Edge e = new Edge(v1, v2, weight);
		adjList.putIfAbsent(v1, new LinkedList<>());
		adjList.get(v1).add(e);
		firstVertex.remove(v2.name);

		opAddEdgeCount += 2;
	}
	
	public void addVertex(String name, int cost) {
		addVertex(new Vertex(name, cost));
	}
	
	public void addVertex(Vertex element) {
		vertices.put(element.name, element);
		firstVertex.put(element.name, element);
		opAddVertexCount += 2;
	}
	
	private BigInteger getTotalCost(Vertex v) {
		opCalculateCostCount++;
		if(v.totalCost.equals(BigInteger.ZERO)) {
			BigInteger partialSum = BigInteger.valueOf(v.cost);
			if(adjList.containsKey(v)) {
				for (Edge e : adjList.get(v)) {
					BigInteger weight = BigInteger.valueOf(e.weight);
					weight = weight.multiply(getTotalCost(e.vertex2));
					partialSum = partialSum.add(weight);
				}
			}
			v.totalCost = partialSum;
			return partialSum;
		}
		return v.totalCost;
	}
	
	public BigInteger getTotalCost() {
		if(firstVertex.size() > 1)
			throw new IllegalArgumentException("Há mais de um vértice inicial!");
		
		return getTotalCost((Vertex) firstVertex.values().toArray()[0]);
	}
	
	public boolean containsCycle() {
		for(Vertex v : vertices.values())
			v.mark = 0;
		
		for(Vertex v : vertices.values())
			if(v.mark == 0 && visit(v))
				return true;
		
		return false;
	}
	
	private boolean visit(Vertex v) {
		v.mark = 1;
		if(adjList.containsKey(v))
			for (Edge e : adjList.get(v))
				if (e.vertex2.mark == 1 || e.vertex2.mark == 0 && visit(e.vertex2))
					return true;
		v.mark = 2;
		return false;
	}

	public int[] getOpCount() {
		return new int[] {opAddVertexCount, opAddEdgeCount, opCalculateCostCount};
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("First: ").append(firstVertex.values()).append(System.lineSeparator());
		for(Vertex v : adjList.keySet()) {
			sb.append(v).append(": ");
			for(Edge e : adjList.get(v)) {
				sb.append("[").append(e.vertex2).append("], ");
			}
			sb.append(System.lineSeparator());
		}

		return sb.toString();
	}
	
	public String toGraphViz() {
		StringBuilder sb = new StringBuilder();
		sb.append("digraph G {").append(System.lineSeparator());
		for(Vertex v : adjList.keySet()) {
			for(Edge e : adjList.get(v)) {
				sb.append("    \"")
				.append(v).append("\"")
				.append(" -> \"")
				.append(e.vertex2).append("\"")
				.append(System.lineSeparator());
			}
		}
		sb.append("}").append(System.lineSeparator());
		
		return sb.toString();
	}
}
