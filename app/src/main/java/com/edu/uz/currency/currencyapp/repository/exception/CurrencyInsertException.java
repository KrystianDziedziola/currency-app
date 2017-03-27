package com.edu.uz.currency.currencyapp.repository.exception;


public class CurrencyInsertException extends RuntimeException {

    private static final String MESSAGE_FORMAT = "Cannot insert currency with code %s. It already exists.";

    public CurrencyInsertException(final String currencyCode) {
        super(String.format(MESSAGE_FORMAT, currencyCode));
    }
}
