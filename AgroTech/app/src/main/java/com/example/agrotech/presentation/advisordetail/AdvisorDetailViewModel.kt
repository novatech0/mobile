package com.example.agrotech.presentation.advisordetail

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.agrotech.common.GlobalVariables
import com.example.agrotech.common.Resource
import com.example.agrotech.common.Routes
import com.example.agrotech.common.UIState
import com.example.agrotech.data.repository.advisor.AdvisorRepository
import com.example.agrotech.data.repository.appointment.AvailableDateRepository
import com.example.agrotech.data.repository.profile.ProfileRepository
import kotlinx.coroutines.launch

class AdvisorDetailViewModel(private val navController: NavController, private val profileRepository: ProfileRepository,
                             private val advisorRepository: AdvisorRepository, private val availableDateRepository: AvailableDateRepository
): ViewModel() {

    private val _state = mutableStateOf(UIState<AdvisorDetail>())
    val state: State<UIState<AdvisorDetail>> get() = _state

    // Variable de estado para el mensaje del Snackbar
    private val _snackbarMessage = mutableStateOf<String?>(null)
    val snackbarMessage: State<String?> get() = _snackbarMessage

    fun clearSnackbarMessage() {
        _snackbarMessage.value = null
    }

    fun goBack() {
        navController.popBackStack()
    }

    fun getAdvisorDetail(advisorId: Long) {
        _state.value = UIState(isLoading = true)
        viewModelScope.launch {
            // obtener advisor user_id a partir de la ruta "AdvisorDetail/{userId}"
            val result = advisorRepository.searchAdvisorByAdvisorId(advisorId, GlobalVariables.TOKEN)
            if (result is Resource.Success) {
                val advisor = result.data
                if (advisor != null) {
                    val profileResult = profileRepository.searchProfile(advisor.userId, GlobalVariables.TOKEN)
                    if (profileResult is Resource.Success) {
                        val profile = profileResult.data
                        if (profile != null) {
                            val advisorDetail = AdvisorDetail(
                                id = advisor.id,
                                name = profile.firstName + " " + profile.lastName,
                                description = profile.description,
                                occupation = profile.occupation,
                                experience = profile.experience,
                                rating = advisor.rating,
                                link = profile.photo
                            )
                            _state.value = UIState(data = advisorDetail)
                        }
                    } else {
                        _state.value = UIState(message = "Error cargando la información del asesor")
                    }
                } else {
                    _state.value = UIState(message = "Asesor no encontrado")
                }
            } else {
                _state.value = UIState(message = "Error obteniendo información del asesor")
            }
        }
    }

    fun goToReviewList(advisorId: Long) {
        navController.navigate(Routes.ReviewList.route + "/$advisorId")
    }

    fun goToNewAppointment(advisorId: Long) {
        viewModelScope.launch {
            val result = availableDateRepository.getAvailableDatesByAdvisor(advisorId, GlobalVariables.TOKEN)
            if (result is Resource.Success) {
                val availableDates = result.data
                if (availableDates.isNullOrEmpty()) {
                    _snackbarMessage.value = "No hay fechas disponibles"
                } else {
                    navController.navigate(Routes.NewAppointment.route + "/$advisorId")
                }
            } else {
                _state.value = UIState(message = "Error obteniendo fechas disponibles")
            }
        }
    }
}