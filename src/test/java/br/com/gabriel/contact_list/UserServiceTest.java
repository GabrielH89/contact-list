package br.com.gabriel.contact_list;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.gabriel.contact_list.dtos.CreateContactDto;
import br.com.gabriel.contact_list.entitites.Contact;
import br.com.gabriel.contact_list.repositories.ContactRepository;
import br.com.gabriel.contact_list.services.ContactService;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
	
	@Mock
	private ContactRepository contactRepository;
	
	@InjectMocks
	private ContactService contactService;
	
	@Nested
	class createContact {
		
		@Test
		@DisplayName("It should create a contact with success")
		void shouldCreateContact() {
			var input = new CreateContactDto("username", "imageUrl", "8193673536", "This is a description");
			var expectedContact = new Contact("username", "imageUrl", "8193673536", "This is a description");
			
			when(contactRepository.save(any(Contact.class))).thenReturn(expectedContact);
			
			var savedContact = contactService.createContact(input);
			
			assertNotNull(savedContact);
			assertEquals("username", savedContact.getName());
		    assertEquals("imageUrl", savedContact.getImageUrl());
		    assertEquals("8193673536", savedContact.getTelephoneNumber());
		    assertEquals("This is a description", savedContact.getContactDescription());;
		}
		
		@Test
		@DisplayName("It should throw IllegalArgumentException when required inputs are empty")
		void shouldTryCreateContactWithRequiredEmptyInputs() {
			var input = new CreateContactDto("", "imageUrl", "", "This is a description");
			
			assertThrows(IllegalArgumentException.class, () -> {
				contactService.createContact(input);
			});
		}
		
		@Test
		@DisplayName("It should create a contact with success, even tough not required inputs are empty")
		void shouldCreateContactWithNotRequiredEmptyInputs() {
			var input = new CreateContactDto("Username", "", "829383636", "");
			
			var expectedContact = new Contact("username", "", "8193673536", "");
			
			when(contactRepository.save(any(Contact.class))).thenReturn(expectedContact);
			
			var savedContact = contactService.createContact(input);
			
			assertNotNull(savedContact);
			assertEquals("username", savedContact.getName());
		    assertEquals("", savedContact.getImageUrl());
		    assertEquals("8193673536", savedContact.getTelephoneNumber());
		    assertEquals("", savedContact.getContactDescription());;
		}
	}
	
	@Nested
	class getAllContacts {
		@Test
		@DisplayName("It should return all contacts with sucess")
		void shouldGetAllContacts() {
			var contact1 = new Contact("username", "imageUrl", "8193673536", "This is a description");
			var contact2 = new Contact("Hugo", "imageUrl", "8399503736", "This is other description");
			
			when(contactRepository.findAll()).thenReturn(List.of(contact1, contact2));
			
			var contacts = contactService.getAllContacts();
			
			 assertNotNull(contacts);
			 assertEquals(2, contacts.size());
			 assertEquals("username", contacts.get(0).getName());
			 assertEquals("imageUrl", contacts.get(0).getImageUrl());
			 assertEquals("Hugo", contacts.get(1).getName());
		}
	}
	
	
}
