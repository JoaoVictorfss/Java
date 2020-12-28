package com.mballem.curso.security.web.exception;

import javax.mail.MessagingException;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import com.mballem.curso.security.exception.AcessoNegadoException;

@ControllerAdvice// ouvinte da aplicação
public class ExceptionController {

	//método para capturar uma exceção. Aqui, toda vez que ocorrer essa exeção, o erro cairá aqui
	//#ExceptionHandler deve receber como parâmetro a exceção queremos tratar
	@ExceptionHandler(UsernameNotFoundException.class)
	//exceção do spring para usuário não encontrado
	public ModelAndView usuarioNaoEncontradoException(UsernameNotFoundException ex) {
		ModelAndView model = new ModelAndView("error");
		model.addObject("status", 404);
		model.addObject("error", "Operação não pode ser realizada.");
		model.addObject("message", ex.getMessage());
		return model;
	}
	
	@ExceptionHandler(AcessoNegadoException.class)
	public ModelAndView acessoNegadoException(AcessoNegadoException ex) {
		ModelAndView model = new ModelAndView("error");
		model.addObject("status", 403);
		model.addObject("error", "Operação não pode ser realizada.");
		model.addObject("message", ex.getMessage());
		return model;
	}
}
