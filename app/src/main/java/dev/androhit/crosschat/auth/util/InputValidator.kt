package dev.androhit.crosschat.auth.util

import dev.androhit.crosschat.auth.domain.model.InputValidationError
import dev.androhit.crosschat.domain.model.Result

object InputValidator {

    /**
     * Checks whether any of the provided strings is blank or empty.
     *
     * A string is considered invalid if it is empty or contains only whitespace.
     *
     * @param inputs Variable number of string values to validate
     * @return `true` if at least one input is blank or empty, otherwise `false`
     */
    fun validateBlankInputs(vararg inputs: String): Result<Unit, InputValidationError.GeneralError> {
        return if(inputs.any { it.isBlank() }) {
            Result.Error(InputValidationError.GeneralError.ALL_MANDATORY)
        } else {
            Result.Success(Unit)
        }
    }

    /**
     * Validates a user's name input.
     *
     * Validation rules:
     * - Name must not be blank
     * - Name length must be between 3 and 16 characters (inclusive)
     * - Name may contain only alphabetic characters (A–Z, a–z)
     * - Single spaces are allowed between words (e.g. "John Doe")
     * - Leading, trailing, or multiple consecutive spaces are not allowed
     *
     * @param name The name entered by the user
     * @return [Result.Success] with [Unit] if the name is valid or [Result.Error] with
     * [InputValidationError.NameError] if validation fails
     */
    fun validateName(name: String): Result<Unit, InputValidationError.NameError> {
        val trimmedName = name.trim()

        return when {
            trimmedName.isEmpty() ->
                Result.Error(InputValidationError.NameError.MANDATORY)

            trimmedName.length < 3 ->
                Result.Error(InputValidationError.NameError.TOO_SHORT)

            trimmedName.length > 16 ->
                Result.Error(InputValidationError.NameError.TOO_LONG)

            !trimmedName.matches(Regex("^[A-Za-z]+( [A-Za-z]+)*$")) ->
                Result.Error(InputValidationError.NameError.INVALID_CHARACTERS)

            else ->
                Result.Success(Unit)
        }
    }

    /**
     * Validates user's email address
     *
     * Validation rules:
     * - Email must not be blank
     * - Email must follow a valid format: local-part@domain
     * - Leading and trailing whitespaces are ignored
     *
     * @param email The email address entered by the user
     * @return [Result.Success] with [Unit] if the email is valid,
     * or [Result.Error] with [InputValidationError.EmailError] if validation fails
     */
    fun validateEmail(email: String): Result<Unit, InputValidationError.EmailError> {
        val trimmedEmail = email.trim()

        return when {
            trimmedEmail.isEmpty() ->
                Result.Error(InputValidationError.EmailError.MANDATORY)

            !trimmedEmail.matches(
                Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
            ) ->
                Result.Error(InputValidationError.EmailError.EMAIL_INVALID)

            else ->
                Result.Success(Unit)
        }
    }

    /**
     * Validates a password during registration.
     *
     * Validation rules:
     * - Password must not be blank
     * - Password length must be between 8 and 16 characters
     * - Password must not contain whitespace
     * - Password must be alphanumeric
     *
     * @param password The password entered by the user
     * @return [Result.Success] with [Unit] if the password is valid,
     *         or [Result.Error] with [InputValidationError.PasswordError] if validation fails
     */
    fun validatePassword(password: String): Result<Unit, InputValidationError.PasswordError> {
        return when {
            password.isBlank() ->
                Result.Error(InputValidationError.PasswordError.MANDATORY)

            password.length !in 8..16 ->
                Result.Error(InputValidationError.PasswordError.PASSWORD_LENGTH)

            password.contains(Regex("\\s")) ->
                Result.Error(InputValidationError.PasswordError.WHITESPACE_NOT_ALLOWED)

            !password.contains(Regex("[A-Za-z]")) ->
                Result.Error(InputValidationError.PasswordError.MISSING_ALPHABET)

            !password.contains(Regex("\\d")) ->
                Result.Error(InputValidationError.PasswordError.MISSING_NUMBER)

            else -> Result.Success(Unit)
        }
    }
}