package br.ufpb.dcx.aps.carcassone;

public class Jogador {
	private Cor cor;
	private int meeples;
	private int pontuacao;
	
	public Jogador(Cor cor) {
		this.meeples=7;
		this.cor = cor;
	}

	public Cor getCor() {
		return cor;
	}

	public void setCor(Cor cor) {
		this.cor = cor;
	}

	public int getMeeples() {
		return meeples;
	}

	public void setMeeples(int meeples) {
		this.meeples = meeples;
	}

	public int getPontuacao() {
		return pontuacao;
	}

	public void setPontuacao(int pontuacao) {
		this.pontuacao = pontuacao;
	}
	public String toString() {
		return this.getCor()+"("+this.getPontuacao()+","+this.getMeeples()+")";
	}

}
