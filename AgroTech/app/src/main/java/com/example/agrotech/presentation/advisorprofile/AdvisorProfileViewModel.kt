package com.example.agrotech.presentation.advisorprofile

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
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
import com.example.agrotech.domain.profile.UpdateProfile
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class AdvisorProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
) : ViewModel() {
    private val _state = mutableStateOf(UIState<Profile>())
    val state: State<UIState<Profile>> get() = _state

    private val _isUploadingImage = mutableStateOf(false)
    val isUploadingImage: State<Boolean> get() = _isUploadingImage

    private val _profileId = mutableLongStateOf(0)

    private val _firstName = mutableStateOf("")
    val firstName: State<String> get() = _firstName

    private val _lastName = mutableStateOf("")
    val lastName: State<String> get() = _lastName

    private val _birthDate = mutableStateOf("")
    val birthDate: State<String> get() = _birthDate

    private val _description = mutableStateOf("")
    val description: State<String> get() = _description

    private val _city = mutableStateOf("")
    val city: State<String> get() = _city

    private val _country = mutableStateOf("")
    val country: State<String> get() = _country

    private val _photo = mutableStateOf<File?>(null)
    val photo: State<File?> get() = _photo

    private val _photoUrl = mutableStateOf("")
    val photoUrl: State<String> get() = _photoUrl

    private val _occupation = mutableStateOf("")
    val occupation: State<String> get() = _occupation

    private val _experience = mutableIntStateOf(0)
    val experience: State<Int> get() = _experience

    fun getAdvisorProfile() {
        _state.value = UIState(isLoading = true)
        viewModelScope.launch {
            val result = profileRepository.searchProfile(GlobalVariables.USER_ID, GlobalVariables.TOKEN)
            if (result is Resource.Success) {
                _state.value = UIState(data = result.data)
                _profileId.longValue = result.data?.id ?: 0
                _firstName.value = result.data?.firstName ?: ""
                _lastName.value = result.data?.lastName ?: ""
                _birthDate.value = result.data?.birthDate ?: ""
                _description.value = result.data?.description ?: ""
                _city.value = result.data?.city ?: ""
                _country.value = result.data?.country ?: ""
                _photoUrl.value = result.data?.photo ?: ""
                _occupation.value = result.data?.occupation ?: ""
                _experience.intValue = result.data?.experience ?: 0
            } else {
                _state.value = UIState(message = result.message ?: "Error obteniendo el perfil")
            }
        }
    }

    fun updateAdvisorProfile() {
        _state.value = UIState(isLoading = true)
        viewModelScope.launch {
            val validationError = validateProfileData()
            if (validationError.isNotBlank()) {
                _state.value = UIState(message = validationError)
                return@launch
            }

            val updateProfile = UpdateProfile(
                firstName = _firstName.value,
                lastName = _lastName.value,
                city = _city.value,
                country = _country.value,
                birthDate = _birthDate.value,
                description = _description.value,
                photo = _photo.value,
                occupation = _occupation.value,
                experience = _experience.intValue
            )

            val result = profileRepository.updateProfile(GlobalVariables.TOKEN, _profileId.longValue, updateProfile)
            _state.value = if (result is Resource.Success) {
                UIState(data = result.data)
            }

            else {
                UIState(message = result.message ?: "Error actualizando el perfil")
            }

        }
    }

    private fun validateProfileData(): String {
        if (_firstName.value.isBlank()) return "El nombre no puede estar vacío"
        if (_lastName.value.isBlank()) return "El apellido no puede estar vacío"
        if (_birthDate.value.isBlank()) return "La fecha de nacimiento no puede estar vacía"
        if (_description.value.isBlank()) return "La descripción no puede estar vacía"
        if (_city.value.isBlank()) return "La ciudad no puede estar vacía"
        if (_country.value.isBlank()) return "El país no puede estar vacío"
        if (_occupation.value.isBlank()) return "La ocupación no puede estar vacía"
        if (_experience.intValue < 1) return "La experiencia no puede ser menor a 1 año"

        val datePattern = Regex("""\d{4}-\d{2}-\d{2}""")
        if (!datePattern.matches(_birthDate.value)) return "La fecha debe estar en el formato yyyy-MM-dd"
        return ""
    }

    fun onFirstNameChange(value: String) { _firstName.value = value }
    fun onLastNameChange(value: String) { _lastName.value = value }
    fun onBirthDateChange(value: String) { _birthDate.value = value }
    fun onDescriptionChange(value: String) { _description.value = value }
    fun onCityChange(value: String) { _city.value = value }
    fun onCountryChange(value: String) { _country.value = value }
    fun onOccupationChange(value: String) { _occupation.value = value }
    fun onExperienceChange(value: Int) { _experience.intValue = value }
    fun onPhotoChange(value: File?) { _photo.value = value }
}


