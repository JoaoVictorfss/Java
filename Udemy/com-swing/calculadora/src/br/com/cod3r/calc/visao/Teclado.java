package br.com.cod3r.calc.visao;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import br.com.cod3r.calc.modelo.Memoria;

@SuppressWarnings("serial")
public class Teclado extends JPanel implements ActionListener {
	
	private final Color COR_CINZA_ESCURO = new Color(68, 68, 68);
	private final Color COR_CINZA_CLARO = new Color(99, 99, 99);
	private final Color COR_LARANJA = new Color(242, 163, 60);

	public Teclado() {
		//Como os botões têm tamanhos diferentes, usamos o GridBagLayout, organizamos em linhas e colunas
		GridBagLayout layout = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
		
		setLayout(layout);
		
		//Configurando as constraints
		c.weightx = 1;//peso do eixo x, para ocupar a tela inteira, preencher mais de um espaço
		c.weighty = 1;//peso do eixo y, para ocupar a tela inteira, preencher mais de um espaço
		c.fill = GridBagConstraints.BOTH;//Para preencher o campo todo, expandir no eixo x e y
		
		// Linha 1
		c.gridwidth = 2;//ocupa 2 espaços(largura)
		adicionarBotao("AC", COR_CINZA_ESCURO, c, 0, 0);
		c.gridwidth = 1;//ocupa 1 espaço(largura)
		adicionarBotao("±", COR_CINZA_ESCURO, c, 2, 0);
		adicionarBotao("/", COR_LARANJA, c, 3, 0);

		// Linha 2
		adicionarBotao("7", COR_CINZA_CLARO, c, 0, 1);
		adicionarBotao("8", COR_CINZA_CLARO, c, 1, 1);
		adicionarBotao("9", COR_CINZA_CLARO, c, 2, 1);
		adicionarBotao("*", COR_LARANJA, c, 3, 1);
		
		// Linha 3
		adicionarBotao("4", COR_CINZA_CLARO, c, 0, 2);
		adicionarBotao("5", COR_CINZA_CLARO, c, 1, 2);
		adicionarBotao("6", COR_CINZA_CLARO, c, 2, 2);
		adicionarBotao("-", COR_LARANJA, c, 3, 2);
		
		// Linha 4
		adicionarBotao("1", COR_CINZA_CLARO, c, 0, 3);
		adicionarBotao("2", COR_CINZA_CLARO, c, 1, 3);
		adicionarBotao("3", COR_CINZA_CLARO, c, 2, 3);
		adicionarBotao("+", COR_LARANJA, c, 3, 3);
		
		// Linha 5
		c.gridwidth = 2;//ocupa 2 espaços(largura)
		adicionarBotao("0", COR_CINZA_CLARO, c, 0, 4);
		c.gridwidth = 1;//ocupa 1 espaço(largura)
		adicionarBotao(",", COR_CINZA_CLARO, c, 2, 4);
		adicionarBotao("=", COR_LARANJA, c, 3, 4);
	}

	//Função que organiza o botão na tela, posicionando e adicionando o evento
	private void adicionarBotao(String texto, Color cor, 
			GridBagConstraints c, int x, int y) {
		c.gridx = x;
		c.gridy = y;
		Botao botao = new Botao(texto, cor);
		botao.addActionListener(this);//Adciona o botão para escutar o evento
		add(botao, c);//Adiciona o botão no componente
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		//Escuta os eventos dos botões
		if(e.getSource() instanceof JButton) {
			JButton botao = (JButton) e.getSource();
			//Obtém o valor do botão
			Memoria.getInstancia().processarComando(botao.getText());
		}
	}
}
