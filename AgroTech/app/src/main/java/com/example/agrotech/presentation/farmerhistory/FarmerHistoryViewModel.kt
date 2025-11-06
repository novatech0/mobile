package com.example.agrotech.presentation.farmerhistory

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
import com.example.agrotech.data.repository.appointment.AppointmentRepository
import com.example.agrotech.data.repository.farmer.FarmerRepository
import com.example.agrotech.data.repository.profile.ProfileRepository
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class FarmerHistoryViewModel(
    private val navController: NavController,
    private val profileRepository: ProfileRepository,
    private val advisorRepository: AdvisorRepository,
    private val appointmentRepository: AppointmentRepository,
    private val farmerRepository: FarmerRepository
) : ViewModel() {

    private val _state = mutableStateOf(UIState<List<AppointmentCard>>())
    val state: State<UIState<List<AppointmentCard>>> get() = _state

    fun goBack() {
        navController.popBackStack()
    }

    fun onReviewAppointment(appointmentId: Long) {
        navController.navigate(Routes.FarmerReviewAppointment.route + "/$appointmentId")
    }

    fun getFarmerHistory(selectedDate: Date? = null) {
        _state.value = UIState(isLoading = true)
        viewModelScope.launch {
            val farmerResult = farmerRepository.searchFarmerByUserId(GlobalVariables.USER_ID, GlobalVariables.TOKEN)

            if (farmerResult is Resource.Success && farmerResult.data != null) {
                val farmerId = farmerResult.data.id // Si la búsqueda del granjero fue exitosa
                val result = appointmentRepository.getAppointmentsByFarmer(farmerId, GlobalVariables.TOKEN) // Obtiene las citas del granjero

                if (result is Resource.Success) {
                    var appointments = result.data?.filter { it.status == "COMPLETED" || it.status == "REVIEWED" }

                    // Aplicar filtro de fecha si se ha proporcionado una fecha seleccionada
                    if (selectedDate != null) {
                        val formattedSelectedDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(selectedDate)
                        appointments = appointments?.filter {
                            it.scheduledDate.startsWith(formattedSelectedDate)
                        }
                    }

                    // Ordenar las citas por fecha (scheduledDate) y luego por hora de inicio (startTime)
                    appointments = appointments?.sortedWith(
                        compareBy(
                            { SimpleDateFormat("yyyy-MM-dd").parse(it.scheduledDate) },
                            { SimpleDateFormat("HH:mm").parse(it.startTime) }
                        )
                    )

                    if (!appointments.isNullOrEmpty()) {
                        val appointmentCards = mutableListOf<AppointmentCard>()
                        for (appointment in appointments) {
                            val advisorResult = advisorRepository.searchAdvisorByAdvisorId(appointment.advisorId, GlobalVariables.TOKEN)
                            
                            val advisorName = if (advisorResult is Resource.Success) {
                                val advisor = advisorResult.data
                                val profileResult = advisor?.userId?.let { userId ->
                                    profileRepository.searchProfile(userId, GlobalVariables.TOKEN)
                                }
                                if (profileResult is Resource.Success) {
                                    val profile = profileResult.data
                                    "${profile?.firstName ?: "Asesor"} ${profile?.lastName ?: "Desconocido"}"
                                } else {
                                    "Asesor Desconocido"
                                }
                            } else {
                                "Asesor Desconocido"
                            }

                            val advisorPhoto = if (advisorResult is Resource.Success) {
                                val advisor = advisorResult.data
                                val profileResult = advisor?.userId?.let { userId ->
                                    profileRepository.searchProfile(userId, GlobalVariables.TOKEN)
                                }
                                if (profileResult is Resource.Success) {
                                    val profile = profileResult.data
                                    profile?.photo ?: "Asesor Desconocido"
                                } else {
                                    "Asesor Desconocido"
                                }
                            } else {
                                "Asesor Desconocido"
                            }

                            appointmentCards.add(
                                AppointmentCard(
                                    id = appointment.id,
                                    advisorName = advisorName,
                                    advisorPhoto = advisorPhoto,
                                    message = appointment.message,
                                    status = appointment.status,
                                    scheduledDate = appointment.scheduledDate,
                                    startTime = appointment.startTime,
                                    endTime = appointment.endTime,
                                    meetingUrl = appointment.meetingUrl
                                )
                            )
                        }
                        _state.value = UIState(data = appointmentCards)
                    } else {
                        _state.value = UIState(message = "No se encontraron citas previas")
                    }
                } else if (result is Resource.Error) {
                    _state.value = UIState(message = "Error al intentar obtener las citas")
                }
            } else {
                _state.value = UIState(message = "Error al intentar obtener información del usuario")
            }
        }
    }
}
