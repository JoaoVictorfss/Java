package com.mballem.curso.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.mballem.curso.security.domain.PerfilTipo;
import com.mballem.curso.security.service.UsuarioService;

//classe que configura o spring security
@EnableGlobalMethodSecurity(prePostEnabled = true)//habilitando o uso de anotations para a parte de segurança, adicionamos no agendamento controller
@EnableWebSecurity 
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	private static final String ADMIN = PerfilTipo.ADMIN.getDesc();
    private static final String MEDICO = PerfilTipo.MEDICO.getDesc();
    private static final String PACIENTE = PerfilTipo.PACIENTE.getDesc();
	
	@Autowired
	private UsuarioService service;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		//Nossa própria configuração de segurança
		http.authorizeRequests()
			// acessos públicos liberados
			.antMatchers("/webjars/**", "/css/**", "/image/**", "/js/**").permitAll()//liberando arquivos estáticos
			.antMatchers("/", "/home").permitAll()//liberando o acesso à página home(não precisa de autenticação)
			.antMatchers("/u/novo/cadastro", "/u/cadastro/realizado", "/u/cadastro/paciente/salvar").permitAll()
			.antMatchers("/u/confirmacao/cadastro").permitAll()
			.antMatchers("/u/p/**").permitAll()
			
			// acessos privados admin
			.antMatchers("/u/editar/senha", "/u/confirmar/senha").hasAnyAuthority(PACIENTE, MEDICO)
			.antMatchers("/u/**").hasAuthority(ADMIN)
			
			// acessos privados medicos
			.antMatchers("/medicos/especialidade/titulo/*").hasAnyAuthority(PACIENTE, MEDICO)
			.antMatchers("/medicos/dados", "/medicos/salvar", "/medicos/editar").hasAnyAuthority(MEDICO, ADMIN)
			.antMatchers("/medicos/**").hasAuthority(MEDICO)
			
			// acessos privados pacientes
			.antMatchers("/pacientes/**").hasAuthority(PACIENTE)			
			
			// acessos privados especialidades
			.antMatchers("/especialidades/datatables/server/medico/*").hasAnyAuthority(MEDICO, ADMIN)
			.antMatchers("/especialidades/titulo").hasAnyAuthority(MEDICO, ADMIN, PACIENTE)
			.antMatchers("/especialidades/**").hasAuthority(ADMIN)

				// O spring security sabe qual é o perfil de quem tá acessando a aplicação por causa da função loadUserByUsername, pois passamos
				// a lista de perfis
			.anyRequest().authenticated()
				//login
				.and()//and usado para concatenar instruções
				.formLogin()
				.loginPage("/login")
				.defaultSuccessUrl("/", true) //para onde a aplicação deve direcionar ao ter sucesso no login
				.failureUrl("/login-error")//caso dê erro ao fazer login
				.permitAll()
			.and()
				.logout()
				.logoutSuccessUrl("/")
				//capturando o erro ao ter o acesso negado
				.and()
				.exceptionHandling()//capturando a exceção
				.accessDeniedPage("/acesso-negado")
				//Remember me => Tem como objetivo criar um cookie no navegador que vai ser guardado em cash, duração de duas semanas como padrão(dá para alterar)
				//Mesmo ao desligar o pc ou fechar o navegador, continuaremos logados no site por causa das informações guardados
				//Para isso, ao fazer login, tenho que marcar a opção remember-me. Ativamos no input da pagina de login  com name e id = "remember-me"
				.and()
				.rememberMe();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		//Criptogrando a senha para comparar a senha com a senha do formulário de login
		auth.userDetailsService(service).passwordEncoder(new BCryptPasswordEncoder());
	}
}
