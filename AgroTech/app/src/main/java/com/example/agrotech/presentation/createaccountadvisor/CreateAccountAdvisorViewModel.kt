package com.example.agrotech.presentation.createaccountadvisor

import androidx.compose.runtime.State
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.agrotech.common.Routes
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.example.agrotech.common.GlobalVariables
import com.example.agrotech.common.Resource
import com.example.agrotech.common.UIState
import com.example.agrotech.data.repository.authentication.AuthenticationRepository
import com.example.agrotech.domain.profile.CreateProfile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class CreateAccountAdvisorViewModel(
    private val navController: NavController,
    private val authenticationRepository: AuthenticationRepository
) : ViewModel() {

    // Variables de estado para los campos de texto
    var firstName = mutableStateOf("")
        private set
    var lastName = mutableStateOf("")
        private set
    var email = mutableStateOf("")
        private set
    var birthDate = mutableStateOf("")
        private set
    var password = mutableStateOf("")
        private set
    var city = mutableStateOf("")
        private set
    var country = mutableStateOf("")
        private set

    // Function to create a Profile object with the current values
    fun getProfile(): CreateProfile {
        return CreateProfile(
            userId = GlobalVariables.USER_ID,
            firstName = firstName.value,
            lastName = lastName.value,
            city = city.value,
            country = country.value,
            birthDate = birthDate.value,
            description = "",
            occupation = "",
            photo = File(""), // archivo vacio temporal
            experience = 0
        )
    }

    private val _state = mutableStateOf(UIState<Unit>())
    val state: State<UIState<Unit>> get() = _state

    // Variable de estado para el mensaje del Snackbar
    private val _snackbarMessage = mutableStateOf<String?>(null)
    val snackbarMessage: State<String?> get() = _snackbarMessage

    fun clearSnackbarMessage() {
        _snackbarMessage.value = null
    }

    fun goToLoginScreen() {
        navController.navigate(Routes.SignIn.route)
    }

    private fun goToCreateProfileAdvisorScreen() {
        navController.navigate(Routes.CreateProfileAdvisor.route)
    }

    fun signUp() {
        viewModelScope.launch {
            _state.value = UIState(isLoading = true)
            val emailValue = email.value
            val passwordValue = password.value
            val roles = GlobalVariables.ROLES
            val result = authenticationRepository.signUp(emailValue, passwordValue, roles)
            if (result is Resource.Success) {
                _state.value = UIState(data = Unit)

                // Sign in the user to obtain the token
                signIn(emailValue, passwordValue)
            } else {
                _state.value = UIState(message = result.message ?: "Error al registrarse")
                _snackbarMessage.value = result.message ?: "Error al registrarse"
            }
        }
    }

    private fun signIn(email: String, password: String) {
        viewModelScope.launch {
            val result = authenticationRepository.signIn(email, password)
            if (result is Resource.Success) {
                val token = result.data?.token
                if (token != null) {
                    GlobalVariables.USER_ID = result.data.id
                    GlobalVariables.TOKEN = token
                    withContext(Dispatchers.Main) {
                        goToCreateProfileAdvisorScreen()
                    }
                } else {
                    _state.value = UIState(message = "Error al obtener el token")
                    _snackbarMessage.value = "Error al obtener el token"
                }
            } else {
                _state.value = UIState(message = result.message ?: "Error al iniciar sesión")
                _snackbarMessage.value = result.message ?: "Error al iniciar sesión"
            }
        }
    }
}