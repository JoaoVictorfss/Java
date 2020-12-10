package br.com.cod3r.calc.visao;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;

@SuppressWarnings("serial")
public class Calculadora extends JFrame {

	public Calculadora() {
		
		organizarLayout();
		
		setSize(232, 322);
		//para finalizar a aplicação
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		//Aplicação abre no centro da tela
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	
	private void organizarLayout() {
		//BorderLayout = gerenciador de layout, define a tela em norte, sul e centro
		setLayout(new BorderLayout());
		
		Display display = new Display();
		display.setPreferredSize(new Dimension(233, 60));
		add(display, BorderLayout.NORTH);//display fica no norte
		
		Teclado teclado = new Teclado();
		add(teclado, BorderLayout.CENTER);//teclado fica no centro
		
	}


	public static void main(String[] args) {
		new Calculadora();
	}
}
