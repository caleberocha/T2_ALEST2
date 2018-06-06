package pucrs.calebe.alest2.t2;

import java.math.BigInteger;
import java.util.LinkedList;
import java.util.NoSuchElementException;

/**
 * Grafo direcionado com pesos nas arestas.
 * @author calebe
 */
public class DirectedEdgeWeightedGraph {

	/**
	 * Vertice do grafo.
	 * Composto por nome, custo, marcação de visitado (para detecção de ciclo) e custo total
	 */
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

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + cost;
			result = prime * result + ((name == null) ? 0 : name.hashCode());
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
			Vertex other = (Vertex) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (cost != other.cost)
				return false;
			if (name == null) {
				if (other.name != null)
					return false;
			} else if (!name.equals(other.name))
				return false;
			return true;
		}

		private DirectedEdgeWeightedGraph getOuterType() {
			return DirectedEdgeWeightedGraph.this;
		}
	}

	/**
	 * Aresta do grafo.
	 */
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

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + ((vertex1 == null) ? 0 : vertex1.hashCode());
			result = prime * result + ((vertex2 == null) ? 0 : vertex2.hashCode());
			result = prime * result + weight;
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
			Edge other = (Edge) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (vertex1 == null) {
				if (other.vertex1 != null)
					return false;
			} else if (!vertex1.equals(other.vertex1))
				return false;
			if (vertex2 == null) {
				if (other.vertex2 != null)
					return false;
			} else if (!vertex2.equals(other.vertex2))
				return false;
			if (weight != other.weight)
				return false;
			return true;
		}

		private DirectedEdgeWeightedGraph getOuterType() {
			return DirectedEdgeWeightedGraph.this;
		}
	}

	private MyHashTable<String, Vertex> vertices;				// Dicionario de vertices, onde a chave é o nome
	private MyHashTable<Vertex, LinkedList<Edge>> adjList;		// Lista de arestas iniciadas por cada vértice
	private MyHashTable<String, Vertex> initialVertex;			// Vertice inicial. Inicialmente, recebe todas os vértices. Quando as arestas sao adicionadas, os vertices que são pontos de destino são removidos
	private int opCalculateCostCount;							// Contagem de operações de cálculo de custo total

	/**
	 * Inicializa um grafo com o tamanho especificado.
	 * @param size Quantidade de vertices
	 */
	public DirectedEdgeWeightedGraph(int size) {
		vertices = new MyHashTable<>(size);
		adjList = new MyHashTable<>(size);
		initialVertex = new MyHashTable<>(size);

		opCalculateCostCount = 0;
	}

	/**
	 * Adiciona uma nova aresta.
	 * @param name1 Nome do vertice de origem
	 * @param name2 Nome do vertice de destino
	 * @param weight Peso da aresta
	 */
	public void addEdge(String name1, String name2, int weight) {
		Vertex v1 = vertices.get(name1);
		if(v1 == null)
			throw new NoSuchElementException("Projeto " + name1 + " não encontrado.");
		
		Vertex v2 = vertices.get(name2);
		if(v2 == null)
			throw new NoSuchElementException("Projeto " + name2 + " não encontrado.");
		
		addEdge(v1, v2, weight);
	}

	/**
	 * Adiciona uma nova aresta.
	 * @param v1 Vertice de origem
	 * @param v2 Vertice de destino
	 * @param weight Peso da aresta
	 */
	private void addEdge(Vertex v1, Vertex v2, int weight) {
		Edge e = new Edge(v1, v2, weight);
		adjList.putIfAbsent(v1, new LinkedList<>());
		adjList.get(v1).add(e);
		initialVertex.remove(v2.name);
	}

	/**
	 * Adiciona um novo vértice.
	 * @param name Nome do vértice
	 * @param cost Custo de vértice
	 */
	public void addVertex(String name, int cost) {
		addVertex(new Vertex(name, cost));
	}

	/**
	 * Adicinoa um novo vertice.
	 * @param element Vertice
	 */
	private void addVertex(Vertex element) {
		vertices.put(element.name, element);
		initialVertex.put(element.name, element);
	}

	/**
	 * Calcula o custo total do vértice especificado. O custo é realizado multiplicando o peso da aresta pelo custo
	 * total do vértice adjacente (que por sua vez faz a mesma operação com os adjacentes dele, recursivamente), e por fim adicionando ao custo do vértice.
	 * @param v Vértice
	 * @return Custo total
	 */
	private BigInteger getTotalCost(Vertex v) {
		opCalculateCostCount++;
		// Caso o custo ja tenha sido calculado, apenas le do atributo totalCost, desta forma evitando recalculos desnecessarios
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

	/**
	 * Calcula o custo total do grafo, a partir do vértice inicial. O cálculo somente e realizado se houver apenas um vértice inicial.
	 * @return Custo total
	 */
	public BigInteger getTotalCost() {
		if(initialVertex.size() > 1)
			throw new IllegalArgumentException("Há mais de um vértice inicial!");
		
		return getTotalCost((Vertex) initialVertex.values().toArray()[0]);
	}

	/**
	 * Detecta ciclos no grafo.
	 * @return
	 */
	public boolean containsCycle() {
		for(Vertex v : vertices.values())
			v.mark = 0;
		
		for(Vertex v : vertices.values())
			if(v.mark == 0 && visit(v))
				return true;
		
		return false;
	}

	/**
	 * Visita um vértice. Utilizado pelo método containsCycle().
	 * @param v Vértice
	 * @return
	 */
	private boolean visit(Vertex v) {
		v.mark = 1;
		if(adjList.containsKey(v))
			for (Edge e : adjList.get(v))
				if (e.vertex2.mark == 1 || e.vertex2.mark == 0 && visit(e.vertex2))
					return true;
		v.mark = 2;
		return false;
	}

	/**
	 * Retorna a quantidade de operacoes.
	 * @return Vetor onde a posições correspondem a:<br>
	 * 0 - Operações de adicionar vértice
	 * 1 - Operacões de adicionar aresta
	 * 2 - Operacões de calcular custo total
	 */
	public int[] getOpCount() {
		return new int[] {vertices.getOpCount(), adjList.getOpCount(), opCalculateCostCount};
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("First: ").append(initialVertex.values()).append(System.lineSeparator());
		for(Vertex v : adjList.keySet()) {
			sb.append(v).append(": ");
			for(Edge e : adjList.get(v)) {
				sb.append("[").append(e.vertex2).append("], ");
			}
			sb.append(System.lineSeparator());
		}

		return sb.toString();
	}

	/**
	 * Retorna uma representação do grafo no formato suportado pelo GraphViz (linguagem DOT).
	 * @return
	 */
	public String toGraphViz() {
		StringBuilder sb = new StringBuilder();
		sb.append("digraph G {").append(System.lineSeparator());
		for(Vertex v : adjList.keySet()) {
			for(Edge e : adjList.get(v)) {
				sb.append("    \"")
				.append(v)
				.append("\" -> \"")
				.append(e.vertex2)
				.append("\"")
				.append(System.lineSeparator());
			}
		}
		sb.append("}").append(System.lineSeparator());
		
		return sb.toString();
	}
}
