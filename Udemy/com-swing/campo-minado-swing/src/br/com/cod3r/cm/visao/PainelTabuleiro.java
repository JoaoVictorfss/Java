/**
 * @DESC implementação da tela de tabuleiro
 */

package br.com.cod3r.cm.visao;

import java.awt.GridLayout;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import br.com.cod3r.cm.modelo.Tabuleiro;
//JPanel é um container, agrupador de componentes
@SuppressWarnings("serial")
public class PainelTabuleiro extends JPanel {

	public PainelTabuleiro(Tabuleiro tabuleiro) {
		
		//Define como os componentes visuais vão ser organizados nas tela
		//GridLayout organiza os componentes em linhas e colunas
		setLayout(new GridLayout(
				tabuleiro.getLinhas(), tabuleiro.getColunas()));
		
		//Adiciona um botão para cada campo
		tabuleiro.paraCadaCampo(c -> add(new BotaoCampo(c)));
		
		//Onde mostraremos o resultado para o usuário
		tabuleiro.registrarObservador(e -> {
			
			//SwingUtilities.invokeLaterPa é usado para definir a ordem do que vai ocorrer
			//Aqui, após a interface for atualizada, vai mostrar a mensagem e depois reinicia
			SwingUtilities.invokeLater(() -> {
				if(e.isGanhou()) {
					//caso o usuário ganhar. mostra diálogo com a mensagem "ganhou"
					JOptionPane.showMessageDialog(this, "Ganhou :)");
				} else {
					JOptionPane.showMessageDialog(this, "Perdeu :(");
				}
				
				tabuleiro.reiniciar();
			});
		});
	}
}
