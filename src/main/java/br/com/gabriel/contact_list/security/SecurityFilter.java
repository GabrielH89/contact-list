package br.com.gabriel.contact_list.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import br.com.gabriel.contact_list.entitites.User;
import br.com.gabriel.contact_list.repositories.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class SecurityFilter extends OncePerRequestFilter {

	@Autowired
	TokenService tokenService;
	
	@Autowired
	UserRepository userRepository;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
	    var token = this.recoverToken(request);
	    
	    if (token != null) {
	        var login = tokenService.validate(token);
	        UserDetails user = userRepository.findByEmail(login);
	        
	        if (user != null) {
	            var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
	            SecurityContextHolder.getContext().setAuthentication(authentication);
	            request.setAttribute("userId", ((User) user).getId_user());
	            System.out.println("Usuário autenticado: " + user.getUsername());
	        } else {
	            System.out.println("Usuário não encontrado no banco de dados.");
	        }
	    } else {
	        System.out.println("Token ausente ou inválido.");
	    }

	    filterChain.doFilter(request, response);
	}
	
	private String recoverToken(HttpServletRequest request) {
		var authHeader = request.getHeader("Authorization");
		if(authHeader == null) {
			return null;
		}else{
			return authHeader.replace("Bearer ", "");
		}
	}
}


