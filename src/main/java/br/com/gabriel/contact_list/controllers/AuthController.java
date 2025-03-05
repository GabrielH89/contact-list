package br.com.gabriel.contact_list.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import br.com.gabriel.contact_list.dtos.AuthenticationDto;
import br.com.gabriel.contact_list.dtos.LoginResponseDto;
import br.com.gabriel.contact_list.dtos.RegisterUserDto;
import br.com.gabriel.contact_list.entitites.User;
import br.com.gabriel.contact_list.repositories.UserRepository;
import br.com.gabriel.contact_list.security.TokenService;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*") // Permite chamadas de qualquer origem (opcional)
public class AuthController {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private PasswordEncoder passwordEncoder; 
	
	@Autowired
	private TokenService tokenService;
	
	@PostMapping("/register")
	public ResponseEntity<String> register(@RequestBody RegisterUserDto registerUserDto) {
	    // Validação de campos vazios
	    if (registerUserDto.username() == null || registerUserDto.username().trim().isEmpty() ||
	        registerUserDto.email() == null || registerUserDto.email().trim().isEmpty() || 
	        registerUserDto.password() == null || registerUserDto.password().trim().isEmpty()) {
	        return ResponseEntity.status(400).body("Username, email, and password cannot be empty");
	    }

	    try {
	        // Verifica se o e-mail já está cadastrado
	        if (userRepository.findByEmail(registerUserDto.email()) != null) {
	            return ResponseEntity.badRequest().body("Email já cadastrado!");
	        }

	        // Criptografa a senha
	        String encryptedPassword = passwordEncoder.encode(registerUserDto.password());

	        // Cria e salva o novo usuário
	        User newUser = new User(
	            registerUserDto.username(),
	            registerUserDto.email(),
	            encryptedPassword
	        );
	        userRepository.save(newUser);

	        return ResponseEntity.ok("Usuário registrado com sucesso!");
	    } catch (Exception e) {
	        return ResponseEntity.status(500).body("Erro ao registrar usuário: " + e.getMessage());
	    }
	}

	
	@PostMapping("/login")
	public ResponseEntity<LoginResponseDto> login(@RequestBody AuthenticationDto authDto) {
	    if (authDto.email() == null || authDto.email().trim().isEmpty() ||
	        authDto.password() == null || authDto.password().trim().isEmpty()) {
	        return ResponseEntity.status(400).body(new LoginResponseDto("Email and password cannot be empty"));
	    }

	    var user = userRepository.findByEmail(authDto.email());
	    
	    if (user == null) {
	        return ResponseEntity.status(404).body(new LoginResponseDto("Invalid email or password"));
	    }

	    try {
	        var authenticationToken = new UsernamePasswordAuthenticationToken(authDto.email(), authDto.password());
	        var auth = authenticationManager.authenticate(authenticationToken);
	        var token = tokenService.generateToken((User) auth.getPrincipal());

	        return ResponseEntity.ok(new LoginResponseDto(token));
	    } catch (Exception e) {
	        return ResponseEntity.status(401).body(new LoginResponseDto("Invalid email or password"));
	    }
	}


}
