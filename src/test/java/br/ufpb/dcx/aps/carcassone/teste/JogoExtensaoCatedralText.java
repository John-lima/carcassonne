package br.ufpb.dcx.aps.carcassone.teste;

import static br.ufpb.dcx.aps.carcassone.teste.Assertiva.ocorreExcecao;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static br.ufpb.dcx.aps.carcassone.TilesJogoBase.*;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import br.ufpb.dcx.aps.carcassone.BolsaDeTiles;
import br.ufpb.dcx.aps.carcassone.ExcecaoJogo;
import br.ufpb.dcx.aps.carcassone.JogoExtensao;
import br.ufpb.dcx.aps.carcassone.Jogo;
import br.ufpb.dcx.aps.carcassone.JogoExtensao;
import br.ufpb.dcx.aps.carcassone.Partida;
import br.ufpb.dcx.aps.carcassone.tabuleiro.Tile;

public class JogoExtensaoCatedralText {
	private JogoExtensao jogo;
	private BolsaDeTiles tiles;

	@Before
	public void novoJogo() {
		tiles = mock(BolsaDeTiles.class);
		jogo = new JogoExtensao();
	}

	// teste1
	@Test
	public void posicionarMeepleCidadeSemCidade() {
		mockarTiles(tiles, t30, t29);
		Partida partida = jogo.criarPartida(tiles, AMARELO, VERMELHO);
		partida.finalizarTurno();
		partida.posicionarTile(t30, LESTE);

		ocorreExcecaoJogo(() -> partida.posicionarMeepleCidade(SUL),
				"Impossível posicionar meeple em cidade pois o lado Sul do tile 29 é Campo");

		ocorreExcecaoJogo(() -> partida.posicionarMeepleCidade(LESTE),
				"Impossível posicionar meeple em cidade pois o lado Norte do tile 29 é Estrada");
	}
	// teste2
		@Test
		public void verificarCidade() {
			mockarTiles(tiles, t30);
			Partida partida = jogo.criarPartida(tiles, AMARELO, VERMELHO);

			Assert.assertEquals("30(NO,NE)", partida.getCidades());

			ocorreExcecaoJogo(() -> partida.posicionarMeepleCidade(NORTE), "Impossível posicionar meeple na peça inicial");

			Assert.assertEquals("30(NO,NE)", partida.getCidades());
		}
		// teste3
		@Test
		public void cidadeComDoisTilesMeeple() {
			mockarTiles(tiles, t30, t01);
			Partida partida = jogo.criarPartida(tiles, AMARELO, VERMELHO);

			partida.posicionarTile(t30, NORTE);
			Assert.assertEquals("30(NO,NE) 01(SE,NO,NE,SO)", partida.getCidades());

			partida.posicionarMeepleCidade(SUL);
			Assert.assertEquals("30(NO,NE) 01(SE,NO,NE,SO-AMARELO)", partida.getCidades());

			partida.finalizarTurno();
			Assert.assertEquals("30(NO,NE) 01(SE,NO,NE,SO-AMARELO)", partida.getCidades());

			verificarRelatorioPartida(partida, "Partida_Finalizada", "AMARELO(0,6); VERMELHO(0,7)");
			ocorreExcecaoJogo(() -> partida.relatorioTurno(), "Partida finalizada");
			verificarRelatorioTabuleiro(partida, "30N01N");

		}
		// test4
		@Test
		public void cidadesDesconexasMeeple() {
			mockarTiles(tiles, t30, t29, t21);
			Partida partida = jogo.criarPartida(tiles, AMARELO, VERMELHO);
			girar(partida, 2);

			partida.posicionarTile(t30, NORTE);
			partida.finalizarTurno();
			girar(partida, 1);

			partida.posicionarTile(t30, SUL);
			Assert.assertEquals("30(NO,NE) 29(SE,SO)\n21(NE,SE)", partida.getCidades());

			partida.posicionarMeepleCidade(LESTE);
			Assert.assertEquals("30(NO,NE) 29(Se,SO)\n21(NE,SE-VERMELHO)", partida.getCidades());

			verificarRelatorioPartida(partida, "Em_Andamento", "AMARELO(0,7); VERMELHO(0,6)");
			verificarRelatorioTurno(partida, "VERMELHO", "21S", "Meeple_Posicionado");
			verificarRelatorioTabuleiro(partida, "30N29N/n21S");

		}
		// teste5
		@Test
		public void posicionarMeepleEmCidadeOcupada() {
			mockarTiles(tiles, t30, t01, t29);
			Partida partida = jogo.criarPartida(tiles, AMARELO, VERMELHO);

			partida.posicionarTile(t30, NORTE);
			partida.posicionarMeepleCidade(LESTE);
			Assert.assertEquals("30(NO,NE) 01(SE,NO,NE,SO-AMARELO)", partida.getCidades());
			partida.finalizarTurno();

			girar(partida, 3);
			partida.posicionarTile(t01, LESTE);
			ocorreExcecaoJogo(() -> partida.posicionarMeepleEstrada(OESTE),
					"Impossível posicionar meeple pois a ciadde já  está ocupada pelo meeple AMARELO no lado Leste do tile 01");

			verificarRelatorioPartida(partida, "Em_Andamento", "AMARELO(0,6); VERMELHO(0,7)");
			verificarRelatorioTurno(partida, "AMARELO", "29L", "Tile_Posicionado");
			verificarRelatorioTabuleiro(partida, "30N01N29L");

		}
		// teste6
		@Test
		public void cidadeCompletaSemCatedralComMeeple() {
			mockarTiles(tiles, t30, t01, t29, t28, t27);
			Partida partida = jogo.criarPartida(tiles, AMARELO, VERMELHO);

			partida.posicionarTile(t30, NORTE);
			partida.posicionarMeepleCidade(LESTE);

			partida.finalizarTurno();
			girar(partida, 1);
			partida.posicionarTile(t01, OESTE);

			partida.finalizarTurno();
			girar(partida, 2);
			partida.posicionarTile(t01, NORTE);

			partida.finalizarTurno();
			girar(partida, 3);
			partida.posicionarTile(t01, LESTE);

			verificarRelatorioPartida(partida, "Em_Andamento", "AMARELO(12,7); VERMELHO(0,7)");
		}
		// teste7
		@Test
		public void cidadeCompletaComCatedralComMeeple() {
			mockarTiles(tiles, t30, t73, t29, t28, t27);
			Partida partida = jogo.criarPartida(tiles, AMARELO, VERMELHO);

			partida.posicionarTile(t30, NORTE);
			partida.posicionarMeepleCidade(LESTE);

			partida.finalizarTurno();
			girar(partida, 1);
			partida.posicionarTile(t73, OESTE);

			partida.finalizarTurno();
			girar(partida, 2);
			partida.posicionarTile(t73, NORTE);

			partida.finalizarTurno();
			girar(partida, 3);
			partida.posicionarTile(t73, LESTE);

			verificarRelatorioPartida(partida, "Em_Andamento", "AMARELO(18,7); VERMELHO(0,7)");
		}
		// teste8
		@Test
		public void cidadeCompletaComCatedralSemMeeple() {
			mockarTiles(tiles, t30, t73, t29, t28, t27);
			Partida partida = jogo.criarPartida(tiles, AMARELO, VERMELHO);

			partida.posicionarTile(t30, NORTE);

			partida.finalizarTurno();
			girar(partida, 1);
			partida.posicionarTile(t73, OESTE);

			partida.finalizarTurno();
			girar(partida, 2);
			partida.posicionarTile(t73, NORTE);

			partida.finalizarTurno();
			girar(partida, 3);
			partida.posicionarTile(t73, LESTE);

			verificarRelatorioPartida(partida, "Em_Andamento", "AMARELO(0,7); VERMELHO(0,7)");
		}
		// teste9
		@Test
		public void cidadeCompletaSemCatedralSemMeeple() {
			mockarTiles(tiles, t30, t01, t29, t28, t27);
			Partida partida = jogo.criarPartida(tiles, AMARELO, VERMELHO);

			partida.posicionarTile(t30, NORTE);

			partida.finalizarTurno();
			girar(partida, 1);
			partida.posicionarTile(t01, OESTE);

			partida.finalizarTurno();
			girar(partida, 2);
			partida.posicionarTile(t01, NORTE);

			partida.finalizarTurno();
			girar(partida, 3);
			partida.posicionarTile(t01, LESTE);

			verificarRelatorioPartida(partida, "Em_Andamento", "AMARELO(0,7); VERMELHO(0,7)");
		}
		// teste10
		@Test
		public void finalizarPartidaComcidadeImcompletaSemCatedralSemMeeple() {
			mockarTiles(tiles, t30, t01, t29);
			Partida partida = jogo.criarPartida(tiles, AMARELO, VERMELHO);

			partida.posicionarTile(t30, NORTE);

			partida.finalizarTurno();
			girar(partida, 1);
			partida.posicionarTile(t01, OESTE);

			partida.finalizarTurno();
			partida.finalizarPartida();

			verificarRelatorioPartida(partida, "Partida_Finalizada", "AMARELO(0,7); VERMELHO(0,7)");
		}
		// teste11
		@Test
		public void finalizarPartidaComcidadeImcompletaComCatedralSemMeeple() {
			mockarTiles(tiles, t30, t73, t29);
			Partida partida = jogo.criarPartida(tiles, AMARELO, VERMELHO);

			partida.posicionarTile(t30, NORTE);

			partida.finalizarTurno();
			girar(partida, 1);
			partida.posicionarTile(t73, OESTE);

			partida.finalizarTurno();
			partida.finalizarPartida();

			verificarRelatorioPartida(partida, "Partida_Finalizada", "AMARELO(0,7); VERMELHO(0,7)");
		}

	
	private void girar(Partida partida, int quantidade) {
		for (int i = 0; i < quantidade; i++) {
			partida.girarTile();
		}
	}

	private void ocorreExcecaoJogo(ExceptionThrower et, String mensagem) {
		ocorreExcecao(et).tipoExcecao(ExcecaoJogo.class).mensagem(mensagem);
	}

	private void mockarTiles(BolsaDeTiles mock, Tile primeiro, Tile... tiles) {
		when(mock.pegar()).thenReturn(primeiro, Arrays.copyOf(tiles, tiles.length + 1));
	}

	private void verificarRelatorioPartida(Partida partida, String status, String sequencia) {
		String relatorio = partida.relatorioPartida();
		Assert.assertEquals("Status: " + status + "\nJogadores: " + sequencia, relatorio);
	}

	private void verificarRelatorioTurno(Partida partida, String jogador, String tile, String status) {
		String relatorio = partida.relatorioTurno();
		Assert.assertEquals("Jogador: " + jogador + "\nTile: " + tile + "\nStatus: " + status, relatorio);
	}

	private void verificarRelatorioTabuleiro(Partida partida, String configuracao) {
		Assert.assertEquals(configuracao, partida.relatorioTabuleiro());
	}

}
