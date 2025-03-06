package br.com.gabriel.contact_list.exceptions;

public class MissingFieldException extends RuntimeException {
	public MissingFieldException(String message) {
		super(message);
	}
}
