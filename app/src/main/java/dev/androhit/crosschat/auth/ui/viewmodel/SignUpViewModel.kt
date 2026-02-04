package dev.androhit.crosschat.auth.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.androhit.crosschat.auth.domain.AuthRepository
import dev.androhit.crosschat.auth.domain.model.InputValidationError
import dev.androhit.crosschat.auth.ui.event.SignUpEvent
import dev.androhit.crosschat.auth.ui.state.SignUpUiState
import dev.androhit.crosschat.auth.util.InputValidator
import dev.androhit.crosschat.domain.model.DataError
import dev.androhit.crosschat.domain.model.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SignUpViewModel(
    private val authRepository: AuthRepository
): ViewModel() {
    private val _uiState = MutableStateFlow(SignUpUiState())
    val uiState = _uiState.asStateFlow()

    fun onEvent(e: SignUpEvent) {
        when(e) {
            is SignUpEvent.OnNameChanged -> {
                val error = when(val result = InputValidator.validateName(e.name)) {
                    is Result.Error -> when(result.error) {
                        InputValidationError.NameError.MANDATORY -> "Name is required"
                        InputValidationError.NameError.TOO_LONG -> "Name is too long"
                        InputValidationError.NameError.TOO_SHORT -> "Name is too short"
                        InputValidationError.NameError.INVALID_CHARACTERS -> "Name contains invalid characters"
                    }
                    is Result.Success -> null
                }
                _uiState.update { it.copy(name = e.name, nameError = error) }
            }
            is SignUpEvent.OnEmailChanged -> {
                val error = when(val result = InputValidator.validateEmail(e.email)) {
                    is Result.Error -> when(result.error) {
                        InputValidationError.EmailError.MANDATORY -> "Email is required"
                        InputValidationError.EmailError.EMAIL_INVALID -> "Invalid email address"
                    }
                    is Result.Success -> null
                }
                _uiState.update { it.copy(email = e.email, emailError = error) }
            }
            is SignUpEvent.OnPasswordChanged -> {
                val error = when(val result = InputValidator.validatePassword(e.password)) {
                    is Result.Error -> when(result.error) {
                        InputValidationError.PasswordError.MANDATORY -> "Password is required"
                        InputValidationError.PasswordError.WHITESPACE_NOT_ALLOWED -> "Whitespace not allowed"
                        InputValidationError.PasswordError.PASSWORD_LENGTH -> "Password length must be 8-16"
                        InputValidationError.PasswordError.MISSING_ALPHABET -> "Password must contain letters"
                        InputValidationError.PasswordError.MISSING_NUMBER -> "Password must contain numbers"
                    }
                    is Result.Success -> null
                }
                _uiState.update { it.copy(password = e.password, passwordError = error) }
            }
            is SignUpEvent.OnSubmit -> {
                val error = when(
                    val result = InputValidator.validateBlankInputs(
                        uiState.value.name, uiState.value.email, uiState.value.password
                    )
                ) {
                    is Result.Error -> when(result.error) {
                        InputValidationError.GeneralError.ALL_MANDATORY -> "All fields are mandatory"
                    }
                    is Result.Success -> null
                }
                _uiState.update { it.copy(generalError = error) }

                if(error == null) {
                    _uiState.update { it.copy(isLoading = true) }
                    viewModelScope.launch {
                        when(
                            val result = authRepository.register(
                                uiState.value.name, uiState.value.email, uiState.value.password
                            )
                        ) {
                            is Result.Error -> {
                                val error = when(result.error) {
                                    DataError.Network.TOO_MANY_REQUESTS -> "Please try again later"
                                    DataError.Network.NO_INTERNET -> "No internet connection"
                                    DataError.Network.BAD_REQUEST -> "All fields are mandatory"
                                    DataError.Network.CONFLICT -> "User already exists"
                                    else -> "Something went wrong"
                                }
                                _uiState.update { it.copy(generalError = error, isLoading = false) }
                            }
                            is Result.Success -> {
                                _uiState.update { it.copy(isLoading = false) }
                                e.onSuccess()
                            }
                        }
                    }
                }
            }
        }
    }
}