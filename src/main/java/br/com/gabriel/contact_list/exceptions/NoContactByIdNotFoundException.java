package br.com.gabriel.contact_list.exceptions;

public class NoContactByIdNotFoundException extends RuntimeException {
	public NoContactByIdNotFoundException(String message) {
		super(message);
	}
}
