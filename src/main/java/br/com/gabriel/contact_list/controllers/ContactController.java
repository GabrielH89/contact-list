package br.com.gabriel.contact_list.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.gabriel.contact_list.dtos.CreateContactDto;
import br.com.gabriel.contact_list.entitites.Contact;
import br.com.gabriel.contact_list.services.ContactService;

@RestController
@RequestMapping("/contacts")
public class ContactController {
	
	@Autowired
	private ContactService contactService;
	
	@PostMapping
	public ResponseEntity<Contact> create(@RequestBody CreateContactDto createContactDto) {
		Contact createdContact = contactService.createContact(createContactDto);
		return ResponseEntity.status(HttpStatus.CREATED).body(createdContact);
	}
	
	@GetMapping
	public List<Contact> getAll() {
		List<Contact> contacts = contactService.getAllProducts();
		return contacts;
	}
}
