package com.edu.uz.currency.currencyapp.helper;

import java.io.IOException;

public class RequestExceptions extends IOException {

    public RequestExceptions() {
        super("An HTTP request error occurred.");
    }
}