package br.com.gabriel.contact_list.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.gabriel.contact_list.dtos.CreateContactDto;
import br.com.gabriel.contact_list.dtos.UpdateContactDto;
import br.com.gabriel.contact_list.entitites.Contact;
import br.com.gabriel.contact_list.exceptions.NoContactByIdNotFoundException;
import br.com.gabriel.contact_list.exceptions.NoContactsFoundException;
import br.com.gabriel.contact_list.repositories.ContactRepository;

@Service
public class ContactService {
	
	@Autowired
	private ContactRepository contactRepository;
	
	public Contact createContact(CreateContactDto createContactDto) {
		if (createContactDto.name() == null || createContactDto.name().isEmpty()) {
	        throw new IllegalArgumentException("Name cannot be empty");
		}

	    if (createContactDto.telephoneNumber() == null || createContactDto.telephoneNumber().isEmpty()) {
	        throw new IllegalArgumentException("Telephone number cannot be empty");
	    }
		var entity = new Contact(
				createContactDto.name(), 
				createContactDto.imageUrl(),
				createContactDto.telephoneNumber(),
				createContactDto.contactDescription()
				);
		var contactSaved = contactRepository.save(entity);
		return contactSaved;
	}
	
	public List<Contact> getAllContacts() {
		var contacts = contactRepository.findAll();
		if(contacts.isEmpty()) {
			throw new NoContactsFoundException("No contacts found");
		}else{
			return contacts;
		}
	}
	
	public Contact getContactById(long id_contact) {
		var contact = contactRepository.findById(id_contact);
		if(contact.isPresent()) {
			return contact.get();
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
	
	public Contact updateContactById(long id_contact, UpdateContactDto updateContactDto) {
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
	        
			return contactRepository.save(contact);
		}else{
			throw new NoContactByIdNotFoundException("Contact not found with this id");
		}
	}
}
