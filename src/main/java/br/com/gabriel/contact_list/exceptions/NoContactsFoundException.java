package br.com.gabriel.contact_list.exceptions;

public class NoContactsFoundException extends RuntimeException {
    // Construtor que recebe uma mensagem de erro
    public NoContactsFoundException(String message) {
        super(message);
    }
}
