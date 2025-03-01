package br.com.gabriel.contact_list.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import br.com.gabriel.contact_list.entitites.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	UserDetails findByEmail(String email);
}
