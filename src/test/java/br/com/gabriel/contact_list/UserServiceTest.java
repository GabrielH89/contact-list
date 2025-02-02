package br.com.gabriel.contact_list;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.gabriel.contact_list.dtos.CreateContactDto;
import br.com.gabriel.contact_list.dtos.UpdateContactDto;
import br.com.gabriel.contact_list.entitites.Contact;
import br.com.gabriel.contact_list.exceptions.NoContactByIdNotFoundException;
import br.com.gabriel.contact_list.exceptions.NoContactsFoundException;
import br.com.gabriel.contact_list.repositories.ContactRepository;
import br.com.gabriel.contact_list.services.ContactService;
import static org.mockito.Mockito.*;

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
		@DisplayName("It should return all contacts with success")
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
		
		@Test
		@DisplayName("It should return exception hanler message if there are no users")
		void shouldThrowExceptionWhenNoContact() {
			when(contactRepository.findAll()).thenReturn(List.of());
			
			assertThrows(NoContactsFoundException.class, () -> contactService.getAllContacts());
		}
	}
	
	@Nested 
	class getContactById {
		@Test
		@DisplayName("It should return an user by id with success")
		void shouldGetUserById() {
			var contact1 = new Contact("Gabriel", "imageUrl", "8193673536", "This is a description");
			var idContact1 = contact1.getId_contact();
			
			when(contactRepository.findById(idContact1)).thenReturn(Optional.of(contact1));
			
			var contact = contactService.getContactById(idContact1);
			assertNotNull(contact);
			assertEquals("Gabriel", contact.getName());
			assertEquals("imageUrl", contact.getImageUrl());
			assertEquals("8193673536", contact.getTelephoneNumber());
			assertEquals("This is a description", contact.getContactDescription());
		}
	}
	
		@Test
		@DisplayName("It should throw an exception message if idUser not found")
		void shouldThrowExceptionWhenNoidContact() {
			var contact1 = new Contact("Gabriel", "imageUrl", "8193673536", "This is a description");
			var contactId = contact1.getId_contact();
			
			//O contato não é salvo no mock do contactRepository
			when(contactRepository.findById(contactId)).thenReturn(Optional.empty());
			
			assertThrows(NoContactByIdNotFoundException.class, () -> contactService.getContactById(contactId));	
		}
		
		@Nested
		class deleteAllContacts {
			@Test
			@DisplayName("It should delete all contacts with success")
			void shouldDeleteAllContacts() {
				var contact1 = new Contact("username", "imageUrl", "8193673536", "This is a description");
				var contact2 = new Contact("Hugo", "imageUrl", "8399503736", "This is other description");
				
				when(contactRepository.findAll()).thenReturn(List.of(contact1, contact2));
				
				contactService.deleteAllContacts();
				
				Mockito.verify(contactRepository).deleteAll();
			}
			
			@Test
			@DisplayName("It should throw an exception if there are no contacts")
			void shouldThrowExceptionWhenNoContactsToDelete() {
				when(contactRepository.findAll()).thenReturn(List.of());
				
				assertThrows(NoContactsFoundException.class, () -> contactService.deleteAllContacts());
			}
		}
		
		@Nested
		class deleteContactById {
			@Test
			@DisplayName("It should delete a contact by id with sucess")
			void shouldDeleteContactById() {
				var contact = new Contact("username", "imageUrl", "8193673536", "This is a description");
				var idContact = contact.getId_contact();
				when(contactRepository.findById(idContact)).thenReturn(Optional.of(contact));
				
				contactService.deleteContactId(idContact);
				Mockito.verify(contactRepository).deleteById(idContact);
			}
		}
		
	 		@Test
		    @DisplayName("It should throw an exception message if the contact does not exist")
		    void shouldThrowExceptionWhenContactNotFound() {
		        var idContact = 1L; // ID fictício para simular um contato inexistente
		        
		        when(contactRepository.findById(idContact)).thenReturn(Optional.empty());

		        assertThrows(NoContactByIdNotFoundException.class, () -> contactService.deleteContactId(idContact));

		        // Garante que o método deleteById nunca foi chamado
		        Mockito.verify(contactRepository, Mockito.never()).deleteById(idContact);
		    }
	 		
	 	@Nested
	 	class updateContactById {
	 		@Test
			@DisplayName("It should update a contact with success")
			void shouldUpdtaeContact() {
	 			var updateContact = new UpdateContactDto("Julia", "newImage", "839404737", "This is other description");

	 	        var existingContact = new Contact("username", "imageUrl", "8193673536", "This is a description");
	 	        existingContact.setId_contact(1L);

	 	        when(contactRepository.findById(1L)).thenReturn(Optional.of(existingContact));
	 	        when(contactRepository.save(any(Contact.class))).thenAnswer(invocation -> invocation.getArgument(0)); 

	 	        // Act
	 	        var updatedContact = contactService.updateContactById(1L, updateContact);

	 	        // Assert
	 	        assertEquals("Julia", updatedContact.getName());
	 	        assertEquals("newImage", updatedContact.getImageUrl());
	 	        assertEquals("839404737", updatedContact.getTelephoneNumber());
	 	        assertEquals("This is other description", updatedContact.getContactDescription());

	 	        verify(contactRepository).save(any(Contact.class));
			}
	 		
	 		@Test
	 		@DisplayName("It should throw an exception when updating a contact with empty required inputs")
	 		void shouldThrowExceptionWhenUpdatingContactWithEmptyRequiredInputs() {
	 		    // Arrange
	 		    var updateContact = new UpdateContactDto("", "newImage", "", "This is other description");
	 		    
	 		    var existingContact = new Contact("username", "imageUrl", "8193673536", "This is a description");
	 		    existingContact.setId_contact(1L);
	 		    
	 		    when(contactRepository.findById(1L)).thenReturn(Optional.of(existingContact));

	 		    assertThrows(IllegalArgumentException.class, () -> contactService.updateContactById(1L, updateContact));

	 		    verify(contactRepository, never()).save(any(Contact.class));
	 		}
	 		
	 		@Test
	 		@DisplayName("It should throw an exception after trying update a contact with invalid id")
	 		void shouldThrowExceptionWhenAfterTryingUpdateWithInvalidId() {
	 			 var updateContact = new UpdateContactDto("Julia", "newImage", "819836362", "This is other description");

	 		    when(contactRepository.findById(1L)).thenReturn(Optional.empty()); 

	 		    // Act & Assert
	 		    assertThrows(NoContactByIdNotFoundException.class, () -> contactService.updateContactById(1L, updateContact));

	 		    verify(contactRepository, never()).save(any(Contact.class)); 
	 		}

	 	}
}
