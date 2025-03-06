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
import br.com.gabriel.contact_list.exceptions.MissingFieldException;
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
	
	private void validateFields(String message, String... fields) {
		for(String field : fields) {
			if(field == null || field.trim().isEmpty()) {
				throw new MissingFieldException(message);
			}
		}
	}
	
	public ShowContactDto createContact(CreateContactDto createContactDto, HttpServletRequest request) {
		validateFields("Name and telephoneNumber cannot be empty", createContactDto.name(), createContactDto.telephoneNumber());
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
	
	public void deleteContactId(long id_contact, HttpServletRequest request) {
	    long userId = (Long) request.getAttribute("userId");
	    var contact = contactRepository.findById(id_contact)
	        .orElseThrow(() -> new NoContactByIdNotFoundException("Contact not found"));

	    if (contact.getUser().getId_user() != userId) {
	        throw new SecurityException("You are not authorized to delete this contact");
	    }

	    contactRepository.deleteById(id_contact);
	}
	
	public ShowContactDto updateContactById(long id_contact, UpdateContactDto updateContactDto, HttpServletRequest request) {
	    long userId = (Long) request.getAttribute("userId");
	    var contact = contactRepository.findById(id_contact)
	        .orElseThrow(() -> new NoContactByIdNotFoundException("Contact not found"));

	    if (contact.getUser().getId_user() != userId) {
	        throw new SecurityException("You are not authorized to update this contact");
	    }
	    
	    validateFields("Name and telephoneNumber cannot be empty", updateContactDto.name(), updateContactDto.telephoneNumber());
	    // Atualiza os dados
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
	        contact.getContactDescription()
	    );
	}

}
