package com.edu.uz.currency.currencyapp.helper;

import java.io.IOException;

public class RequestException extends IOException {

    public RequestException() {
        super("An HTTP request error occurred.");
    }
}