package com.capgemini.wsb.fitnesstracker.mail.api;

public class EmailSendingException extends RuntimeException {
    public EmailSendingException(String message, Throwable cause) {
        super(message, cause);
    }
}