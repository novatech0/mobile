package com.example.agrotech.presentation.createprofileadvisor

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.agrotech.common.GlobalVariables
import com.example.agrotech.common.Resource
import com.example.agrotech.common.Routes
import com.example.agrotech.common.UIState
import com.example.agrotech.data.repository.profile.ProfileRepository
import com.example.agrotech.domain.profile.Profile
import com.example.agrotech.presentation.createaccountadvisor.CreateAccountAdvisorViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class CreateProfileAdvisorViewModel(
    private val navController: NavController,
    private val profileRepository: ProfileRepository,
    private val createAccountAdvisorViewModel: CreateAccountAdvisorViewModel,
) : ViewModel() {

    // Variables de estado para los campos de texto
    private val _photoUrl = mutableStateOf("")
    val photoUrl: State<String> get() = _photoUrl

    private val _description = mutableStateOf("")
    val description: State<String> get() = _description

    private val _occupation = mutableStateOf("")
    val occupation: State<String> get() = _occupation

    private val _experience = mutableStateOf(0)
    val experience: State<Int> get() = _experience

    private val _photo = mutableStateOf<File?>(null)
    val photo: State<File?> get() = _photo

    private val _state = mutableStateOf(UIState<Profile>())
    val state: State<UIState<Profile>> get() = _state

    // Variable de estado para el mensaje del Snackbar
    private val _snackbarMessage = mutableStateOf<String?>(null)
    val snackbarMessage: State<String?> get() = _snackbarMessage

    fun clearSnackbarMessage() {
        _snackbarMessage.value = null
    }

    fun goToLoginScreen() {
        navController.navigate(Routes.SignIn.route)
    }

    private fun goToConfirmationAccountAdvisorScreen() {
        navController.navigate(Routes.ConfirmCreationAccountAdvisor.route)
    }

    fun createProfile() {
        viewModelScope.launch {
            val token = GlobalVariables.TOKEN
            if (token.isBlank()) {
                _state.value = UIState(message = "Un token es requerido")
                _snackbarMessage.value = "Un token es requerido"
                return@launch
            }

            _state.value = UIState(isLoading = true)

            if (_photo.value == null || _description.value.isBlank() || _occupation.value.isBlank() || _experience.value == 0) {
                _state.value = UIState(message = "Todos los campos son requeridos")
                _snackbarMessage.value = "Todos los campos son requeridos"
                return@launch
            }

            val profile = createAccountAdvisorViewModel.getProfile().copy(
                description = _description.value,
                occupation = _occupation.value,
                photo = _photo.value!!,
                experience = experience.value
            )
            val result = profileRepository.createProfile(token, profile, false)
            withContext(Dispatchers.Main) {
                if (result is Resource.Success) {
                    _state.value = UIState(data = result.data)
                    goToConfirmationAccountAdvisorScreen()
                } else {
                    _state.value = UIState(message = result.message ?: "Error al crear perfil")
                    _snackbarMessage.value = result.message ?: "Error al crear perfil"
                }
            }
        }
    }

    fun onPhotoChanged(photo: File) {
        _photo.value = photo
    }

    fun onDescriptionChanged(description: String) {
        _description.value = description
    }

    fun onOccupationChanged(occupation: String) {
        _occupation.value = occupation
    }

    fun onExperienceChanged(experience: Int) {
        _experience.value = experience
    }
}