package com.example.agrotech.presentation.login

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.agrotech.common.GlobalVariables
import com.example.agrotech.common.Resource
import com.example.agrotech.common.UIState
import com.example.agrotech.data.repository.advisor.AdvisorRepository
import com.example.agrotech.data.repository.authentication.AuthenticationRepository
import com.example.agrotech.domain.authentication.AuthenticationResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authenticationRepository: AuthenticationRepository,
    private val advisorRepository: AdvisorRepository
) : ViewModel() {

    private val _state = mutableStateOf(UIState<Unit>())
    val state: State<UIState<Unit>> get() = _state

    private val _email = mutableStateOf("")
    val email: State<String> get() = _email

    private val _password = mutableStateOf("")
    val password: State<String> get() = _password

    private val _isPasswordVisible = mutableStateOf(false)
    val isPasswordVisible: State<Boolean> get() = _isPasswordVisible

    fun signIn(
        onSuccess: (isAdvisor: Boolean) -> Unit,
    ) {
        _state.value = UIState(isLoading = true)

        viewModelScope.launch {
            when (val result = authenticationRepository.signIn(_email.value, _password.value)) {
                is Resource.Success -> {
                    GlobalVariables.TOKEN = result.data?.token ?: ""
                    GlobalVariables.USER_ID = result.data?.id ?: 0

                    if (GlobalVariables.TOKEN.isBlank() || GlobalVariables.USER_ID == 0L) {
                        _state.value = UIState(isLoading = false)
                        return@launch
                    }
                    // Guarda usuario localmente
                    authenticationRepository.insertUser(
                        AuthenticationResponse(
                            id = GlobalVariables.USER_ID,
                            username = _email.value,
                            token = GlobalVariables.TOKEN
                        )
                    )
                    _email.value = ""
                    _password.value = ""
                    // Verifica si es asesor
                    val isAdvisor = advisorRepository.isUserAdvisor(GlobalVariables.USER_ID, GlobalVariables.TOKEN)
                    _state.value = UIState(isLoading = false)
                    if (isAdvisor) onSuccess(true) else onSuccess(false)
                }
                is Resource.Error -> {
                    _state.value = UIState(isLoading = false)
                }
            }
        }
    }

    fun clearError() {
        _state.value = UIState(message = "")
    }

    fun setEmail(email: String) {
        _email.value = email
    }

    fun setPassword(password: String) {
        _password.value = password
    }

    fun togglePasswordVisibility() {
        _isPasswordVisible.value = !_isPasswordVisible.value
    }
}