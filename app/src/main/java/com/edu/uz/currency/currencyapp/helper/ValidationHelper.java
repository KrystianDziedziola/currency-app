package com.edu.uz.currency.currencyapp.helper;

public class ValidationHelper {

    private static final String WRONG_CODE = "Wrong code '%s'. It should contain 3 letters";
    private static final String WRONG_VALUE = "Wrong value '%s' of field '%s'. It should be bigger than '%s'";
    private static final String NULL_PARAMETER = "Parameter '%s' shouldn't be null";
    private static final String CURRENCY_REGEX = "[A-Za-z]{3}";

    public static void notNull(final String name, final Object object) {
        if (object == null) {
            throw new IllegalArgumentException(String.format(NULL_PARAMETER, name));
        }
    }

    public static void validateCurrencyCode(final String code) {
        if (!code.matches(CURRENCY_REGEX)) {
            throw new IllegalArgumentException(String.format(WRONG_CODE, code));
        }
    }

    public static void validateMinimumValue(final String name, final double value, final int minimumValue) {
        if (value <= minimumValue) {
            throw new IllegalArgumentException(String.format(WRONG_VALUE, value, name, minimumValue));
        }
    }
}
