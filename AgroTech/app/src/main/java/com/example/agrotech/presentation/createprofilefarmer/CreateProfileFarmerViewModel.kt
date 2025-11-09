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
import com.example.agrotech.data.repository.authentication.RegistrationDataRepository
import com.example.agrotech.data.repository.profile.ProfileRepository
import com.example.agrotech.domain.profile.CreateProfile
import com.example.agrotech.domain.profile.Profile
import com.example.agrotech.presentation.createaccountfarmer.CreateAccountFarmerViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

@HiltViewModel
class CreateProfileFarmerViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val registrationDataRepository: RegistrationDataRepository,
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

    fun createProfile(onSuccess: () -> Unit) {
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
            val profile = buildProfile()
            val result = profileRepository.createProfile(token, profile, false)
            withContext(Dispatchers.Main) {
                if (result is Resource.Success) {
                    _state.value = UIState(data = result.data)
                    onSuccess()
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

    private fun buildProfile(): CreateProfile {
        return CreateProfile(
            userId = registrationDataRepository.userId,
            firstName = registrationDataRepository.firstName,
            lastName = registrationDataRepository.lastName,
            city = registrationDataRepository.city,
            country = registrationDataRepository.country,
            birthDate = registrationDataRepository.birthDate,
            description = _description.value,
            photo = _photo.value ?: File(""),
            occupation = "",
            experience = 0
        )
    }


}