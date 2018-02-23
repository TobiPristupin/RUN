package com.tobipristupin.simplerun.interfaces;

/**
 * Class used to represent hierarchical structure of errors with enums.
 */

public interface ErrorType {

    enum EmailLogin implements ErrorType {
        INVALID_EMAIL, REQUIRED_FIELD
    }

    enum PasswordLogin implements ErrorType {
        SHORT_PASSWORD, REQUIRED_FIELD, INVALID_CREDENTIALS, PASSWORD_DONT_MATCH
    }
}
