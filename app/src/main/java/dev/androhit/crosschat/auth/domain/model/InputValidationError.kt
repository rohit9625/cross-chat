package dev.androhit.crosschat.auth.domain.model

import dev.androhit.crosschat.domain.model.Error

sealed interface InputValidationError: Error {
    enum class GeneralError: InputValidationError {
        ALL_MANDATORY,
    }

    enum class NameError: InputValidationError {
        MANDATORY,
        TOO_LONG,
        TOO_SHORT,
        INVALID_CHARACTERS
    }

    enum class PasswordError: InputValidationError {
        MANDATORY,
        WHITESPACE_NOT_ALLOWED,
        PASSWORD_LENGTH,
        MISSING_ALPHABET,
        MISSING_NUMBER,
    }

    enum class EmailError: InputValidationError {
        MANDATORY,
        EMAIL_INVALID,
    }
}