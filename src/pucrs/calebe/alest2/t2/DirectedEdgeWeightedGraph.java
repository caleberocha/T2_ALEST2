package pucrs.calebe.alest2.t2;

import java.math.BigInteger;
import java.util.*;

/**
 * Grafo direcionado com pesos nas arestas.
 * @author calebe
 */
public class DirectedEdgeWeightedGraph {

	/**
	 * Vertice do grafo.
	 * Composto por nome, custo, marcaçao de visitado (para detecçao de ciclo) e custo total
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
	}

	private Map<String, Vertex> vertices;				// Dicionario de vertices, onde a chave e o nome
	private Map<Vertex, LinkedList<Edge>> adjList;		// Lista de adjacencias de cada vertice. As adjacencias sao arestas
	private Map<String, Vertex> firstVertex;			// Vertice inicial. Inicialmente, recebe todas os vertices. Quando as arestas sao adicionadas, os vertices que sao pontos de destino sao removidos.
	private int opAddVertexCount; 						// Contagem de operacoes de adicionar vertice
	private int opAddEdgeCount; 						// Contagem de operacoes de adicionar aresta
	private int opCalculateCostCount;					// Contagem de operacoes de calculo de custo total

	/**
	 * Inicializa um grafo com o tamanho especificado.
	 * @param size Quantidade de vertices
	 */
	public DirectedEdgeWeightedGraph(int size) {
		vertices = new HashMap<>();
		adjList = new HashMap<>();
		firstVertex = new HashMap<>();

		opAddEdgeCount = 0;
		opAddVertexCount = 0;
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
		firstVertex.remove(v2.name);

		opAddEdgeCount += 2;
	}

	/**
	 * Adiciona um novo vertice.
	 * @param name Nome do vertice
	 * @param cost Custo de vertice
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
		firstVertex.put(element.name, element);
		opAddVertexCount += 2;
	}

	/**
	 * Calcula o custo total do vertice especificado. O custo e realizado multiplicando o peso da aresta pelo custo
	 * total do vertice adjacente (que por sua vez faz a mesma operacao com os adjacentes dele, recursivamente), e adicionando ao custo do vertice.
	 * @param v Vertice
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
	 * Calcula o custo total do grafo, a partir do vertice inicial. O calculo somente e realizado se houver apenas um vertice inicial.
	 * @return Custo total
	 */
	public BigInteger getTotalCost() {
		if(firstVertex.size() > 1)
			throw new IllegalArgumentException("Há mais de um vértice inicial!");
		
		return getTotalCost((Vertex) firstVertex.values().toArray()[0]);
	}

	/**
	 * Detecta se ha algum ciclo no grafo.
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
	 * Visita um grafo. Utilizado pelo metodo containsCycle().
	 * @param v Vertice
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
	 * @return Vetor onde a posicoes correspondem a:<br>
	 * 0 - Operaçoes de adicionar vertice
	 * 1 - Operacoes de adicionar aresta
	 * 2 - Operacoes de calcular custo total
	 */
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

	/**
	 * Retorna uma representaçao do grafo no formato suportado pelo GraphViz (linguagem DOT).
	 * @return
	 */
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
