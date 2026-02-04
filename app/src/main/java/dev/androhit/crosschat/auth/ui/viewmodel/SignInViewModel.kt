package dev.androhit.crosschat.auth.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.androhit.crosschat.auth.domain.AuthRepository
import dev.androhit.crosschat.auth.domain.model.InputValidationError
import dev.androhit.crosschat.auth.ui.event.SignInEvent
import dev.androhit.crosschat.auth.ui.state.SignInUiState
import dev.androhit.crosschat.auth.util.InputValidator
import dev.androhit.crosschat.domain.model.DataError
import dev.androhit.crosschat.domain.model.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SignInViewModel(
    private val authRepository: AuthRepository
): ViewModel() {
    private val _uiState = MutableStateFlow(SignInUiState())
    val uiState = _uiState.asStateFlow()

    fun onEvent(e: SignInEvent) {
        when(e) {
            is SignInEvent.OnEmailChanged -> {
                val error = when(val result = InputValidator.validateEmail(e.email)) {
                    is Result.Error -> when(result.error) {
                        InputValidationError.EmailError.MANDATORY -> "Email is required"
                        InputValidationError.EmailError.EMAIL_INVALID -> "Invalid email address"
                    }
                    is Result.Success -> null
                }
                _uiState.update { it.copy(email = e.email, emailError = error) }
            }

            is SignInEvent.OnPasswordChanged -> _uiState.update { it.copy(password = e.password) }

            is SignInEvent.OnSubmit -> {
                val error = when(
                    val result = InputValidator.validateBlankInputs(
                        uiState.value.email, uiState.value.password
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
                            val result = authRepository.login(
                                uiState.value.email, uiState.value.password
                            )
                        ) {
                            is Result.Error -> {
                                val error = when(result.error) {
                                    DataError.Network.TOO_MANY_REQUESTS -> "Please try again later"
                                    DataError.Network.NO_INTERNET -> "No internet connection"
                                    DataError.Network.BAD_REQUEST -> "All fields are mandatory"
                                    DataError.Network.UNAUTHORIZED -> "Invalid email or password"
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