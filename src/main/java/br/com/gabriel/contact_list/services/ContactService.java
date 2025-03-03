package br.com.gabriel.contact_list.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.gabriel.contact_list.dtos.CreateContactDto;
import br.com.gabriel.contact_list.dtos.ShowContactDto;
import br.com.gabriel.contact_list.dtos.UpdateContactDto;
import br.com.gabriel.contact_list.entitites.Contact;
import br.com.gabriel.contact_list.entitites.User;
import br.com.gabriel.contact_list.exceptions.NoContactByIdNotFoundException;
import br.com.gabriel.contact_list.exceptions.NoContactsFoundException;
import br.com.gabriel.contact_list.repositories.ContactRepository;
import br.com.gabriel.contact_list.repositories.UserRepository;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class ContactService {
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ContactRepository contactRepository;
	
	public ShowContactDto createContact(CreateContactDto createContactDto, HttpServletRequest request) {
	    if (createContactDto.name() == null || createContactDto.name().isEmpty()) {
	        throw new IllegalArgumentException("Name cannot be empty");
	    }

	    if (createContactDto.telephoneNumber() == null || createContactDto.telephoneNumber().isEmpty()) {
	        throw new IllegalArgumentException("Telephone number cannot be empty");
	    }

	    // Extrai o ID do usuário do contexto da requisição
	    long userId = (Long) request.getAttribute("userId");

	    // Verifica se o usuário existe
	    User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

	    // Cria o novo contato e associa ao usuário
	    var entity = new Contact(
	            createContactDto.name(), 
	            createContactDto.imageUrl(),
	            createContactDto.telephoneNumber(),
	            createContactDto.contactDescription()
	    );
	    entity.setUser(user); // Associa o contato ao usuário autenticado

	    // Salva o contato
	    var contactSaved = contactRepository.save(entity);
	    return new ShowContactDto(contactSaved.getId_contact(), contactSaved.getName(), contactSaved.getImageUrl(), contactSaved.getTelephoneNumber(), 
	    		contactSaved.getContactDescription());
	}
	
	public List<ShowContactDto> getAllContacts() {
		var contacts = contactRepository.findAll();
		if(contacts.isEmpty()) {
			throw new NoContactsFoundException("No contacts found");
		}else{
			return contacts.stream()
					.map(contact -> new ShowContactDto(
							contact.getId_contact(), 
							contact.getName(), 
							contact.getImageUrl(), 
							contact.getTelephoneNumber(), 
							contact.getContactDescription()))
					.collect(Collectors.toList());
		}
	}
	
	public ShowContactDto getContactById(long id_contact) {
		var contact = contactRepository.findById(id_contact);
		if(contact.isPresent()) {
			var contactEntity = contact.get();
			return new ShowContactDto(
					contactEntity.getId_contact(), 
					contactEntity.getName(), 
					contactEntity.getImageUrl(), 
					contactEntity.getTelephoneNumber(), 
					contactEntity.getContactDescription());
		}else{
			throw new NoContactByIdNotFoundException("Contact not found");
		}
	}
	
	public List<Contact> deleteAllContacts() {
		var contacts = contactRepository.findAll();
		if(contacts.isEmpty()) {
			throw new NoContactsFoundException("No contacts found");
		}else{
			contactRepository.deleteAll();
			return contactRepository.findAll();
		}
	}
	
	public Contact deleteContactId(long id_contact) {
	    var contact = contactRepository.findById(id_contact);
	    if (contact.isPresent()) {  
	        contactRepository.deleteById(id_contact); 
	        return contact.get();  
	    } else {
	        throw new NoContactByIdNotFoundException("Contact no found to delete"); 
	    }
	}
	
	public ShowContactDto updateContactById(long id_contact, UpdateContactDto updateContactDto) {
		var contactExists = contactRepository.findById(id_contact);
		if(contactExists.isPresent()) {
			var contact = contactExists.get();
			
			if(updateContactDto.name() == null || updateContactDto.name().isEmpty()) {
				throw new IllegalArgumentException("Name cannot be empty");
			}
			
	        if (updateContactDto.telephoneNumber() == null || updateContactDto.telephoneNumber().isEmpty()) {
	        	throw new IllegalArgumentException("Telephone cannot be empty");
	        }
	        
	        contact.setName(updateContactDto.name());
	        contact.setImageUrl(updateContactDto.imageUrl());
	        contact.setTelephoneNumber(updateContactDto.telephoneNumber());
	        contact.setContactDescription(updateContactDto.contactDescription());
	        
	        contact = contactRepository.save(contact);
	        
			return new ShowContactDto(
					contact.getId_contact(), 
					contact.getName(), 
					contact.getImageUrl(), 
					contact.getTelephoneNumber(), 
					contact.getContactDescription());
		}else{
			throw new NoContactByIdNotFoundException("Contact not found with this id");
		}
	}
}
