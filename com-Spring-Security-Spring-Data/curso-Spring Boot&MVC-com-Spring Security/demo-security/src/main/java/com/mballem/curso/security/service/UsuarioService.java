package com.mballem.curso.security.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

import com.mballem.curso.security.datatables.Datatables;
import com.mballem.curso.security.datatables.DatatablesColunas;
import com.mballem.curso.security.domain.Perfil;
import com.mballem.curso.security.domain.PerfilTipo;
import com.mballem.curso.security.domain.Usuario;
import com.mballem.curso.security.exception.AcessoNegadoException;
import com.mballem.curso.security.repository.UsuarioRepository;

@Service
public class UsuarioService implements UserDetailsService {

	@Autowired
	private UsuarioRepository repository;
	@Autowired
	private Datatables datatables;
	@Autowired
	private EmailService emailService;
	
	@Transactional(readOnly = true)
	public Usuario buscarPorEmail(String email) {
		
		return repository.findByEmail(email);
	}

	//validar os dados de login
	@Override @Transactional(readOnly = true)//pra ser gerenciado por uma transação
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Usuario usuario = buscarPorEmailEAtivo(username)//Busca o usuário pelo email no banco de dados
				.orElseThrow(() -> new UsernameNotFoundException("Usuario " + username + " não encontrado."));
		//retorna o usuário. Spring Security valida as credenciais pra saber se estão válidas
		return new User(
			usuario.getEmail(),
			usuario.getSenha(),
			AuthorityUtils.createAuthorityList(getAtuthorities(usuario.getPerfis()))
		);
	}

	//retorna os perfis
	private String[] getAtuthorities(List<Perfil> perfis) {
		String[] authorities = new String[perfis.size()];
		for (int i = 0; i < perfis.size(); i++) {
			authorities[i] = perfis.get(i).getDesc();
		}
		return authorities;
	}

	//busca todos os usuários ou por email
	@Transactional(readOnly = true)
	public Map<String, Object> buscarTodos(HttpServletRequest request) {
		datatables.setRequest(request);
		datatables.setColunas(DatatablesColunas.USUARIOS);
		Page<Usuario> page = datatables.getSearch().isEmpty()
				? repository.findAll(datatables.getPageable())//busca todos
				: repository.findByEmailOrPerfil(datatables.getSearch(), datatables.getPageable());
		return datatables.getResponse(page);
	}

	//cadastro de usuário
	@Transactional(readOnly = false)
	public void salvarUsuario(Usuario usuario) {
		String crypt = new BCryptPasswordEncoder().encode(usuario.getSenha());
		usuario.setSenha(crypt);

		repository.save(usuario); 	 	
	}

	@Transactional(readOnly = true)
	public Usuario buscarPorId(Long id) {
		
		return repository.findById(id).get();
	}

	@Transactional(readOnly = true)
	public Usuario buscarPorIdEPerfis(Long usuarioId, Long[] perfisId) {
		//caso não exista o usuário, usamos o orElseThrow, que lançará uma exceção(tratada na classe ExceptionController) do tipo UsernameNotFoundException, que é disponibilizado pleo Optional retornado no serviço de usuário
		return repository.findByIdAndPerfis(usuarioId, perfisId)
				.orElseThrow(() -> new UsernameNotFoundException("Usuário inexistente!"));
	}

	public static boolean isSenhaCorreta(String senhaDigitada, String senhaArmazenada) {
		//verifica se as duas senhas são iguais, sendo a senha Digitada nõa criptografada e a senha armazenada sim
		return new BCryptPasswordEncoder().matches(senhaDigitada, senhaArmazenada);
	}

	@Transactional(readOnly = false)
	public void alterarSenha(Usuario usuario, String senha) {
		usuario.setSenha(new BCryptPasswordEncoder().encode(senha));
		repository.save(usuario);		
	}

	@Transactional(readOnly = false)
	public void salvarCadastroPaciente(Usuario usuario) throws MessagingException {
		String crypt = new BCryptPasswordEncoder().encode(usuario.getSenha());
		usuario.setSenha(crypt);
		usuario.addPerfil(PerfilTipo.PACIENTE);
		repository.save(usuario);	
		
		emailDeConfirmacaoDeCadastro(usuario.getEmail());
	}

	//Para saber se o usuário existe e está ativo
	@Transactional(readOnly = true)
	public Optional<Usuario> buscarPorEmailEAtivo(String email) {
		return repository.findByEmailAndAtivo(email);
	}

	//recebe o e-mail do usuário que está fazendo o cadasstro
	public void emailDeConfirmacaoDeCadastro(String email) throws MessagingException {
		//Base64Utils => serve para transformar o e-mail em um código, porque o e-mail será enviado como um parâmetro da url, o encodeToString recebe um array de caracteres
		String codigo = Base64Utils.encodeToString(email.getBytes());
		//email como email de destino e codigo como o email transformado
		emailService.enviarPedidoDeConfirmacaoDeCadastro(email, codigo);
	}
	
	@Transactional(readOnly = false)
	public void ativarCadastroPaciente(String codigo) {
		//converte o e-mail codificado para o formato originals
		String email = new String(Base64Utils.decodeFromString(codigo));

		Usuario usuario = buscarPorEmail(email);
		if (usuario.hasNotId()) {
			throw new AcessoNegadoException("Não foi possível ativar seu cadastro. Entre em "
					+ "contato com o suporte.");
		}
		usuario.setAtivo(true);
	}

	@Transactional(readOnly = false)
	public void pedidoRedefinicaoDeSenha(String email) throws MessagingException {
		Usuario usuario = buscarPorEmailEAtivo(email)
				.orElseThrow(() -> new UsernameNotFoundException("Usuario " + email + " não encontrado."));;
		
		String verificador = RandomStringUtils.randomAlphanumeric(6);
		
		usuario.setCodigoVerificador(verificador);
		
		emailService.enviarPedidoRedefinicaoSenha(email, verificador);
	}
	
	
	
}
