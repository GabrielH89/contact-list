package br.com.gabriel.contact_list.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.gabriel.contact_list.entitites.Contact;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long>{

}
