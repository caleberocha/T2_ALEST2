package pucrs.calebe.alest2.t2;

public class Projeto {
	private String nome;
	private int custo;
	public Projeto(String nome, int custo) {
		super();
		this.nome = nome;
		this.custo = custo;
	}
	
	public String getNome() {
		return nome;
	}

	public int getCusto() {
		return custo;
	}
	
	
	@Override
	public String toString() {
		return "Projeto [nome=" + nome + ", custo=" + custo + "]";
	}
	
	
}
