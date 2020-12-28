package views.conta;

import java.awt.BorderLayout;

import views.templates.Painel;

public class TelaCadastroContaSal extends javax.swing.JFrame {
	public TelaCadastroContaSal() {
		organizarLayout();

		setTitle("Cadastro de Conta");

		// Tamanho da tela
		setSize(550, 550);

		// Aplicação abre no centro da tela
		setLocationRelativeTo(null);

		// Fica Visível
		setVisible(true);
	}

	void organizarLayout() {
		Painel painelConta = new Painel("Cadastro de Conta");
		add(painelConta, BorderLayout.NORTH);

		FormContaSalario form = new FormContaSalario(this);
		add(form);
	}
}