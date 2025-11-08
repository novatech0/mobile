package com.example.agrotech.presentation.createprofilefarmer

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
import com.example.agrotech.presentation.createaccountfarmer.CreateAccountFarmerViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class CreateProfileFarmerViewModel(
    private val navController: NavController,
    private val profileRepository: ProfileRepository,
    private val createAccountFarmerViewModel: CreateAccountFarmerViewModel
) : ViewModel() {

    // Variables de estado para los campos de texto
    private val _photo = mutableStateOf<File?>(null)
    val photo: State<File?> get() = _photo

    private val _description = mutableStateOf("")
    val description: State<String> get() = _description

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

    private fun goToConfirmationAccountFarmerScreen() {
        navController.navigate(Routes.ConfirmCreationAccountFarmer.route)
    }

    fun createProfile() {
        viewModelScope.launch {
            val token = GlobalVariables.TOKEN
            if (token.isBlank()) {
                _state.value = UIState(message = "Un token es requerido")
                _snackbarMessage.value = "Un token es requerido"
                return@launch
            }

            if (photo.value == null) {
                _state.value = UIState(message = "Una foto es requerida")
                _snackbarMessage.value = "Una foto es requerida"
                return@launch
            }

            _state.value = UIState(isLoading = true)
            val profile = createAccountFarmerViewModel.getProfile().copy(
                description = description.value,
                photo = photo.value!!
            )
            val result = profileRepository.createProfile(token, profile, false)
            withContext(Dispatchers.Main) {
                if (result is Resource.Success) {
                    _state.value = UIState(data = result.data)
                    goToConfirmationAccountFarmerScreen()
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

}