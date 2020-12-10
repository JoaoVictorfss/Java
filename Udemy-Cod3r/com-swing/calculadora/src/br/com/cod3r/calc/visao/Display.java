package br.com.cod3r.calc.visao;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;

import br.com.cod3r.calc.modelo.Memoria;
import br.com.cod3r.calc.modelo.MemoriaObservador;

@SuppressWarnings("serial")
public class Display extends JPanel implements MemoriaObservador {
	//Vai escutar o evento 
	
	private final JLabel label;
	
	public Display() {
		//Display se increve na Memoria(observador) que esta interessado em ser notificado 
		//sempre que o valor for modificado
		Memoria.getInstancia().adicionarObservador(this);
		
		setBackground(new Color(46, 49, 50));
		label = new JLabel(Memoria.getInstancia().getTextoAtual());
		label.setForeground(Color.WHITE);//cor da font
		label.setFont(new Font("courier", Font.PLAIN, 30));
		
		//Alinha o layout do label à direita e mais abaixo
		setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 25));//10 como valorna vertical e 25 na horizontal
		
		add(label);
	}
	
	@Override
	public void valorAlterado(String novoValor) {
		//Escuta o evento de valor Alterado para modificar o texto do display
		label.setText(novoValor);
	}
}
