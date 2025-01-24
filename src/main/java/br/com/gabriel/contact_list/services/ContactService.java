package br.com.gabriel.contact_list.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.gabriel.contact_list.dtos.CreateContactDto;
import br.com.gabriel.contact_list.entitites.Contact;
import br.com.gabriel.contact_list.repositories.ContactRepository;

@Service
public class ContactService {
	
	@Autowired
	private ContactRepository contactRepository;
	
	public Contact createContact(CreateContactDto createContactDto) {
		var entity = new Contact(
				createContactDto.name(), 
				createContactDto.imageUrl(),
				createContactDto.telephoneNumber(),
				createContactDto.contactDescription()
				);
		var contactSaved = contactRepository.save(entity);
		return contactSaved;
	}
	
	public List<Contact> getAllProducts() {
		var contacts = contactRepository.findAll();
		if(contacts.isEmpty()) {
			System.out.println("Users not found");
			return contacts;
		}else{
			return contacts;
		}
	}
}
