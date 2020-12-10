/**
 * @DESC mostra a tela principal do jogo
 */
package br.com.cod3r.cm.visao;

import javax.swing.JFrame;

import br.com.cod3r.cm.modelo.Tabuleiro;

@SuppressWarnings("serial")
public class TelaPrincipal extends JFrame {
	
	public TelaPrincipal() {
		//Nível hard, com 60 bombas. XD :)
		Tabuleiro tabuleiro = new Tabuleiro(16, 30, 60);
		add(new PainelTabuleiro(tabuleiro));
		
		setTitle("Campo Minado");
		setSize(690, 438);//Define o tamanho
		setLocationRelativeTo(null);//Centraliza a tela
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);//Ecerra aplicação
		setVisible(true);
	}

	public static void main(String[] args) {
		new TelaPrincipal();
	}
}
