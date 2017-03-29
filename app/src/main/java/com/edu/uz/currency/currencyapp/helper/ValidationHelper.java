package com.edu.uz.currency.currencyapp.helper;

public class ValidationHelper {

    private static final String WRONG_CODE = "Wrong code '%s'. It should contain 3 characters";
    private static final String WRONG_RATE = "Wrong rate: '%s'. It should be bigger than 0";
    private static final String NULL_PARAMETER = "Parameter '%s' shouldn't be null";

    public static void notNull(final String name, final Object object) {
        if (object == null) {
            throw new IllegalArgumentException(String.format(NULL_PARAMETER, name));
        }
    }

    public static void validateCurrencyCode(final String code, final int expectedLength) {
        if (code.length() != expectedLength) {
            throw new IllegalArgumentException(String.format(WRONG_CODE, code));
        }
    }

    public static void validateCurrencyRate(final double rate, final int minimumRate) {
        if (rate <= minimumRate) {
            throw new IllegalArgumentException(String.format(WRONG_RATE, rate));
        }
    }
}
