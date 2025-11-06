package com.example.agrotech.presentation.advisorappointments

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.agrotech.common.GlobalVariables
import com.example.agrotech.common.Resource
import com.example.agrotech.common.Routes
import com.example.agrotech.data.repository.advisor.AdvisorRepository
import com.example.agrotech.data.repository.appointment.AppointmentRepository
import com.example.agrotech.data.repository.authentication.AuthenticationRepository
import com.example.agrotech.data.repository.farmer.FarmerRepository
import com.example.agrotech.data.repository.profile.ProfileRepository
import com.example.agrotech.domain.appointment.Appointment
import com.example.agrotech.domain.authentication.AuthenticationResponse
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch

class AdvisorAppointmentsViewModel(
    private val navController: NavController,
    private val authenticationRepository: AuthenticationRepository,
    private val advisorRepository: AdvisorRepository,
    private val appointmentRepository: AppointmentRepository,
    private val profileRepository: ProfileRepository,
    private val farmerRepository: FarmerRepository,
) : ViewModel() {

    var appointments by mutableStateOf<List<Appointment>>(emptyList())
        private set

    var farmerNames by mutableStateOf<Map<Long, String>>(emptyMap())
        private set

    var farmerImagesUrl by mutableStateOf<Map<Long, String>>(emptyMap())
        private set

    var advisorId by mutableStateOf<Long?>(null)
        private set

    var isLoading by mutableStateOf(true)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    private val _expanded = mutableStateOf(false)
    val expanded: State<Boolean> get() = _expanded


    fun loadAppointmentsAgain() {
        loadAppointments()
    }

    fun loadAppointmentsCompletedAgain() {
        loadAppointmentsCompleted()
    }


    private fun loadAppointments() {
        viewModelScope.launch {
            if (GlobalVariables.TOKEN.isBlank() || GlobalVariables.USER_ID == 0L) {
                errorMessage = "Usuario no autenticado"
                return@launch
            }

            isLoading = true
            try {
                val advisorResult = advisorRepository.searchAdvisorByUserId(GlobalVariables.USER_ID, GlobalVariables.TOKEN)
                if (advisorResult is Resource.Success) {
                    advisorId = advisorResult.data?.id
                    advisorId?.let {
                        fetchAppointments(it)
                        fetchFarmerNamesAndImages()
                    }
                } else {
                    errorMessage = advisorResult.message
                }
            } catch (e: Exception) {
                errorMessage = "Error cargando citas: ${e.localizedMessage}"
            } finally {
                isLoading = false
            }
        }
    }

    private fun loadAppointmentsCompleted() {
        viewModelScope.launch {
            if (GlobalVariables.TOKEN.isBlank() || GlobalVariables.USER_ID == 0L) {
                errorMessage = "Usuario no autenticado"
                return@launch
            }

            isLoading = true
            try {
                val advisorResult = advisorRepository.searchAdvisorByUserId(GlobalVariables.USER_ID, GlobalVariables.TOKEN)
                if (advisorResult is Resource.Success) {
                    advisorId = advisorResult.data?.id
                    advisorId?.let {
                        fetchAppointmentsCompleted(it)
                        fetchFarmerNamesAndImages()
                    }
                } else {
                    errorMessage = advisorResult.message
                }
            } catch (e: Exception) {
                errorMessage = "Error cargando citas: ${e.localizedMessage}"
            } finally {
                isLoading = false
            }
        }
    }

    private suspend fun fetchAppointments(advisorId: Long) {
        val appointmentsResult = appointmentRepository.getAppointmentsByAdvisor(advisorId, GlobalVariables.TOKEN)

        if (appointmentsResult is Resource.Success) {
            val allAppointments = appointmentsResult.data ?: emptyList()
            appointments = allAppointments.filter { it.status == "PENDING" }
        } else {
            errorMessage = appointmentsResult.message
        }
    }

    private suspend fun fetchAppointmentsCompleted(advisorId: Long) {
        val appointmentsResult = appointmentRepository.getAppointmentsByAdvisor(advisorId, GlobalVariables.TOKEN)

        if (appointmentsResult is Resource.Success) {
            val allAppointments = appointmentsResult.data ?: emptyList()
            appointments = allAppointments.filter { it.status == "COMPLETED" }
        } else {
            errorMessage = appointmentsResult.message
        }
    }


    private suspend fun fetchFarmerNamesAndImages() {
        val farmersNamesMap = mutableMapOf<Long, String>()
        val farmersImagesMap = mutableMapOf<Long, String>()

        val results = appointments.map { appointment ->
            viewModelScope.async {
                try {
                    val farmerResult = farmerRepository.searchFarmerByFarmerId(appointment.farmerId, GlobalVariables.TOKEN)
                    if (farmerResult is Resource.Success) {
                        val profileResult = profileRepository.searchProfile(farmerResult.data?.userId ?: 0L, GlobalVariables.TOKEN)

                        val name = if (profileResult is Resource.Success) {
                            "${profileResult.data?.firstName} ${profileResult.data?.lastName}"
                        } else "Nombre no disponible"

                        val image = if (profileResult is Resource.Success) {
                            profileResult.data?.photo ?: ""
                        } else ""

                        farmersNamesMap[appointment.id] = name
                        farmersImagesMap[appointment.id] = image
                    } else {
                        farmersNamesMap[appointment.id] = "Nombre no encontrado"
                        farmersImagesMap[appointment.id] = ""
                    }
                } catch (e: Exception) {
                    farmersNamesMap[appointment.id] = "Error"
                    farmersImagesMap[appointment.id] = ""
                }
            }
        }

        results.awaitAll()
        farmerNames = farmersNamesMap
        farmerImagesUrl = farmersImagesMap
    }

    fun setExpanded(value: Boolean) {
        _expanded.value = value
    }
    fun goBack() {
        navController.popBackStack()
    }
    fun goToProfile() {
        navController.navigate(Routes.AdvisorProfile.route)
    }
    fun goToAdvisorAppointmentsHistory(){
        navController.navigate(Routes.AppointmentsAdvisorHistoryList.route)
    }
    private fun goToWelcomeSection() {
        navController.navigate(Routes.Welcome.route)
    }
    fun goToAppointmentDetails(appointmentId: Long) {
        navController.navigate(Routes.AdvisorAppointmentDetail.route + "/$appointmentId")
    }
    fun signOut() {
        GlobalVariables.ROLES = emptyList()
        viewModelScope.launch {
            val authResponse = AuthenticationResponse(
                id = GlobalVariables.USER_ID,
                username = "",
                token = GlobalVariables.TOKEN
            )
            authenticationRepository.deleteUser(authResponse)
            goToWelcomeSection()
        }
    }
}
