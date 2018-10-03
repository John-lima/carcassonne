package br.ufpb.dcx.aps.carcassone;

public class JogoExtensao extends Jogo{
	public Partida criarPartida(BolsaDeTiles tiles, Cor... sequencia) {
		if(tiles == null) {
			throw new ExcecaoJogo("A bolsa de tiles deve ser fornecida na criação de uma partida");
		}else if( sequencia.length < 2) {
			throw new ExcecaoJogo("Cada partida deve ter uma sequência de pelo menos dois jogadores");
			
		}
		
		for(int k=0; k< sequencia.length; k++) {
			for(int i=k+1; i< sequencia.length; i++) {
				if(sequencia[k]== sequencia[i]) {
					throw new ExcecaoJogo("Não pode haver repetição de cores na sequência de jogadores");
				}
			}
		}
		
		
		return new Partida(tiles, sequencia);

	}

}
