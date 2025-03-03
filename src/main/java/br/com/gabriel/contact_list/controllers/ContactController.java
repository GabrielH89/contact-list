package br.com.gabriel.contact_list.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.gabriel.contact_list.dtos.CreateContactDto;
import br.com.gabriel.contact_list.dtos.ShowContactDto;
import br.com.gabriel.contact_list.dtos.UpdateContactDto;
import br.com.gabriel.contact_list.entitites.Contact;
import br.com.gabriel.contact_list.services.ContactService;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/contacts")
public class ContactController {
	
	@Autowired
	private ContactService contactService;
	
	 @PostMapping
	 public ResponseEntity<ShowContactDto> createContact(@RequestBody CreateContactDto createContactDto, HttpServletRequest request) {
        ShowContactDto contact = contactService.createContact(createContactDto, request);
        return new ResponseEntity<>(contact, HttpStatus.CREATED);
	 }
	
	@GetMapping
	public ResponseEntity<List<ShowContactDto>> getAll() {
		List<ShowContactDto> contacts = contactService.getAllContacts();
		if(contacts.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}else {
			return ResponseEntity.status(HttpStatus.OK).body(contacts);
		}
	}
	
	@GetMapping("/{id_contact}")
	public ResponseEntity<ShowContactDto> getById(@PathVariable long id_contact) {
		ShowContactDto contact = contactService.getContactById(id_contact);
		return ResponseEntity.status(HttpStatus.OK).body(contact);
	}
	
	@DeleteMapping
	public ResponseEntity<Void> deleteAll() {
		 List<Contact> contacts = contactService.deleteAllContacts();
		 if (contacts == null || contacts.isEmpty()) {
			 return ResponseEntity.status(HttpStatus.NO_CONTENT).build(); 
	     }else {
	    	 return ResponseEntity.status(HttpStatus.OK).build();  
	     }
	}
	
	@DeleteMapping("/{id_contact}")
	public ResponseEntity<Contact> deleteById(@PathVariable long id_contact) {
		Contact contact = contactService.deleteContactId(id_contact);
		if(contact == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}else{
			return ResponseEntity.status(HttpStatus.OK).body(contact);
		}	
	}
	
	@PutMapping("/{id_contact}") 
	public ResponseEntity<ShowContactDto> updateById(@RequestBody UpdateContactDto updateContactDto, @PathVariable long id_contact) {
		ShowContactDto updatedContact = contactService.updateContactById(id_contact, updateContactDto);
		
		if (updatedContact == null) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	    } else {
	        return ResponseEntity.status(HttpStatus.OK).body(updatedContact); 
	    }
	}
}
