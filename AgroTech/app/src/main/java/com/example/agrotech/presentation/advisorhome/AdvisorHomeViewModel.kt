package com.example.agrotech.presentation.advisorhome

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.agrotech.common.GlobalVariables
import com.example.agrotech.common.Resource
import com.example.agrotech.common.Routes
import com.example.agrotech.data.repository.advisor.AdvisorRepository
import com.example.agrotech.data.repository.appointment.AppointmentRepository
import com.example.agrotech.data.repository.appointment.AvailableDateRepository
import com.example.agrotech.data.repository.authentication.AuthenticationRepository
import com.example.agrotech.data.repository.profile.ProfileRepository
import com.example.agrotech.data.repository.farmer.FarmerRepository // Importa el repositorio de farmers
import com.example.agrotech.data.repository.notification.NotificationRepository
import com.example.agrotech.domain.appointment.Appointment
import com.example.agrotech.domain.appointment.AvailableDate
import com.example.agrotech.domain.authentication.AuthenticationResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdvisorHomeViewModel @Inject constructor(
    private val advisorRepository: AdvisorRepository,
    private val appointmentRepository: AppointmentRepository,
    private val availableDateRepository: AvailableDateRepository,
    private val profileRepository: ProfileRepository,
    private val farmerRepository: FarmerRepository,
    private val authenticationRepository: AuthenticationRepository,
    private val notificationRepository: NotificationRepository
) : ViewModel() {

    var appointments by mutableStateOf<List<Appointment>>(emptyList())
        private set

    var availableDates by mutableStateOf<List<AvailableDate>>(emptyList())
        private set

    var advisorId by mutableStateOf<Long?>(null)
        private set

    var isLoading by mutableStateOf(true)
        private set

    private val _expanded = mutableStateOf(false)
    val expanded: State<Boolean> get() = _expanded

    var errorMessage by mutableStateOf<String?>(null)
        private set

    var advisorName by mutableStateOf<String?>(null)
        private set

    var farmerNames by mutableStateOf<Map<Long, String>>(emptyMap())
        private set

    var farmerImagesUrl by mutableStateOf<Map<Long, String>>(emptyMap())
        private set

    private val _notificationCount = mutableStateOf(0)
    val notificationCount: State<Int> get() = _notificationCount

    fun getNotificationCount() {
        viewModelScope.launch {
            val result = notificationRepository.getNotifications(GlobalVariables.USER_ID, GlobalVariables.TOKEN)
            if (result is Resource.Success) {
                _notificationCount.value = result.data?.size ?: 0
            }
        }
    }

    fun loadData() {
        viewModelScope.launch {
            if (GlobalVariables.TOKEN.isBlank() || GlobalVariables.USER_ID == 0L) {
                return@launch
            }
            fetchAdvisorAndAppointments()
        }
    }

    private fun fetchAdvisorAndAppointments() {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
                // Obtener información del asesor
                val advisorResult = advisorRepository.searchAdvisorByUserId(GlobalVariables.USER_ID, GlobalVariables.TOKEN)
                if (advisorResult is Resource.Success) {
                    advisorId = advisorResult.data?.id
                    fetchAdvisorName(GlobalVariables.USER_ID)
                    advisorId?.let {
                        fetchAppointments(it)
                        fetchAvailableDates(it)
                        fetchFarmerNamesAndImages()
                    }
                } else if (advisorResult is Resource.Error) {
                    errorMessage = advisorResult.message
                }
            } catch (e: Exception) {
                errorMessage = e.message
            } finally {
                isLoading = false
            }
        }
    }

    private suspend fun fetchAppointments(advisorId: Long) {
        val appointmentsResult = appointmentRepository.getAppointmentsByAdvisor(advisorId, GlobalVariables.TOKEN)

        if (appointmentsResult is Resource.Success) {
            appointments = appointmentsResult.data ?: emptyList()
        } else if (appointmentsResult is Resource.Error) {
            errorMessage = appointmentsResult.message
        }
    }

    private suspend fun fetchAvailableDates(advisorId: Long) {
        val result = availableDateRepository.getAvailableDatesByAdvisor(advisorId, GlobalVariables.TOKEN)
        if (result is Resource.Success) {
            availableDates = result.data ?: emptyList()
        }
    }

    private suspend fun fetchFarmerNamesAndImages() {
        val farmersNamesMap = mutableMapOf<Long, String>()
        val farmersImagesMap = mutableMapOf<Long, String>()

        val farmerDataResults = appointments.map { appointment ->
            viewModelScope.async {
                try {
                    val farmerProfileResult = farmerRepository.searchFarmerByFarmerId(appointment.farmerId, GlobalVariables.TOKEN)

                    if (farmerProfileResult is Resource.Success) {
                        val profileResult = profileRepository.searchProfile(farmerProfileResult.data?.userId ?: 0L, GlobalVariables.TOKEN)

                        val farmerName = if (profileResult is Resource.Success) {
                            "${profileResult.data?.firstName} ${profileResult.data?.lastName}"
                        } else {
                            "Name not found"
                        }

                        val farmerImageUrl = if (profileResult is Resource.Success) {
                            profileResult.data?.photo ?: "Image not found"
                        } else {
                            "Image not found"
                        }

                        farmersNamesMap[appointment.id] = farmerName
                        farmersImagesMap[appointment.id] = farmerImageUrl
                    } else {
                        farmersNamesMap[appointment.id] = "Name not found"
                        farmersImagesMap[appointment.id] = "Image not found"
                    }
                } catch (e: Exception) {
                    errorMessage = "Error fetching farmer profile: ${e.localizedMessage}"
                    farmersNamesMap[appointment.id] = "Name not found"
                    farmersImagesMap[appointment.id] = "Image not found"
                }
            }
        }
        farmerDataResults.awaitAll()

        farmerNames = farmersNamesMap
        farmerImagesUrl = farmersImagesMap
    }


    private suspend fun fetchAdvisorName(userId: Long) {
        val profileResult = profileRepository.searchProfile(userId, GlobalVariables.TOKEN)
        if (profileResult is Resource.Success) {
            advisorName = profileResult.data?.firstName
        } else {
            errorMessage = profileResult.message
        }
    }

    fun signOut() {
        viewModelScope.launch {
            val authResponse = AuthenticationResponse(
                id = GlobalVariables.USER_ID,
                username = "",
                token = GlobalVariables.TOKEN
            )
            authenticationRepository.signOut(authResponse)
        }
    }

    fun setExpanded(value: Boolean) {
        _expanded.value = value
    }
}
