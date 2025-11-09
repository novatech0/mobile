package com.example.agrotech.presentation.farmerappointments

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
import com.example.agrotech.data.repository.appointment.AvailableDateRepository
import com.example.agrotech.data.repository.farmer.FarmerRepository
import com.example.agrotech.data.repository.profile.ProfileRepository
import com.example.agrotech.presentation.farmerhistory.AppointmentCard
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class FarmerAppointmentListViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val advisorRepository: AdvisorRepository,
    private val appointmentRepository: AppointmentRepository,
    private val availableDateRepository: AvailableDateRepository,
    private val farmerRepository: FarmerRepository
) : ViewModel() {

    private val _state = mutableStateOf(UIState<List<AppointmentCard>>())
    val state: State<UIState<List<AppointmentCard>>> get() = _state

    fun getAdvisorAppointmentListByFarmer(selectedDate: Date? = null) {
        _state.value = UIState(isLoading = true)
        viewModelScope.launch {
            try {
                val farmerResult = farmerRepository.searchFarmerByUserId(
                    GlobalVariables.USER_ID,
                    GlobalVariables.TOKEN
                )

                if (farmerResult !is Resource.Success || farmerResult.data == null) {
                    _state.value = UIState(message = "Error al obtener información del usuario")
                    return@launch
                }

                val farmerId = farmerResult.data.id
                val result = appointmentRepository.getAppointmentsByFarmer(
                    farmerId,
                    GlobalVariables.TOKEN
                )

                if (result !is Resource.Success || result.data.isNullOrEmpty()) {
                    _state.value = UIState(message = "No se encontraron citas")
                    return@launch
                }
                val appointments = result.data.filter { it.status == "PENDING" || it.status == "ONGOING" }
                val appointmentCards = mutableListOf<AppointmentCard>()
                for (appointment in appointments) {
                    val availableDateResult = availableDateRepository.getAvailableDateById(
                        appointment.availableDateId,
                        GlobalVariables.TOKEN
                    )
                    val availableDate = if (availableDateResult is Resource.Success)
                        availableDateResult.data else null
                    if (availableDate == null) continue
                    if (selectedDate != null) {
                        val formattedSelectedDate =
                            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(selectedDate)
                        if (!availableDate.scheduledDate.startsWith(formattedSelectedDate)) continue
                    }
                    val advisorResult = advisorRepository.searchAdvisorByAdvisorId(
                        availableDate.advisorId,
                        GlobalVariables.TOKEN
                    )
                    var advisorName = "Asesor Desconocido"
                    var advisorPhoto = ""

                    if (advisorResult is Resource.Success) {
                        val advisor = advisorResult.data
                        val profileResult = advisor?.userId?.let {
                            profileRepository.searchProfile(it, GlobalVariables.TOKEN)
                        }
                        if (profileResult is Resource.Success) {
                            val profile = profileResult.data
                            advisorName = "${profile?.firstName ?: "Asesor"} ${profile?.lastName ?: ""}"
                            advisorPhoto = profile?.photo ?: ""
                        }
                    }
                    appointmentCards.add(
                        AppointmentCard(
                            id = appointment.id,
                            advisorName = advisorName,
                            advisorPhoto = advisorPhoto,
                            message = appointment.message,
                            status = appointment.status,
                            scheduledDate = availableDate.scheduledDate,
                            startTime = availableDate.startTime,
                            endTime = availableDate.endTime,
                            meetingUrl = appointment.meetingUrl
                        )
                    )
                }
                val sortedAppointments = appointmentCards.sortedWith(
                    compareBy(
                        { SimpleDateFormat("yyyy-MM-dd").parse(it.scheduledDate) },
                        { SimpleDateFormat("HH:mm").parse(it.startTime) }
                    )
                )

                if (sortedAppointments.isNotEmpty()) {
                    _state.value = UIState(data = sortedAppointments)
                } else {
                    _state.value = UIState(message = "No se encontraron citas para la fecha seleccionada")
                }
            } catch (e: Exception) {
                _state.value = UIState(message = "Error al obtener citas: ${e.localizedMessage}")
            }
        }
    }

}
