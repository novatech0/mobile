package com.example.agrotech.presentation.newappointment

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.agrotech.common.GlobalVariables
import com.example.agrotech.common.Resource
import com.example.agrotech.common.Routes
import com.example.agrotech.common.UIState
import com.example.agrotech.data.repository.appointment.AppointmentRepository
import com.example.agrotech.data.repository.appointment.AvailableDateRepository
import com.example.agrotech.data.repository.farmer.FarmerRepository
import com.example.agrotech.domain.appointment.Appointment
import com.example.agrotech.domain.appointment.AvailableDate
import kotlinx.coroutines.launch


class NewAppointmentViewModel(private val navController: NavController,
                              private val availableDateRepository: AvailableDateRepository,
                              private val appointmentRepository: AppointmentRepository,
                              private val farmerRepository: FarmerRepository

): ViewModel() {
    private val _state = mutableStateOf(UIState<List<AvailableDate>>())
    val state: State<UIState<List<AvailableDate>>> get() = _state

    private val _comment = mutableStateOf("")
    val comment: State<String> get() = _comment

    private val _isExpanded = mutableStateOf(false)
    val isExpanded: State<Boolean> get() = _isExpanded

    private val _selectedDate = mutableIntStateOf(-1)
    val selectedDate: State<Int> get() = _selectedDate

    fun goBack() {
        navController.popBackStack()
    }

    fun getAvailableDates(advisorId: Long) {
        _state.value = UIState(isLoading = true)
        viewModelScope.launch {
            val result = availableDateRepository.getAvailableDatesByAdvisor(advisorId, GlobalVariables.TOKEN)
            if (result is Resource.Success) {
                val availableDates = result.data ?: run {
                    _state.value = UIState(message = "No se encontraron fechas disponibles para este asesor")
                    return@launch
                }
                _state.value = UIState(data = availableDates)
            } else {
                _state.value = UIState(message = "Error al obtener las fechas disponibles")
            }
        }
    }

    fun createAppointment(advisorId: Long) {
        viewModelScope.launch {
            val farmerResult = farmerRepository.searchFarmerByUserId(GlobalVariables.USER_ID, GlobalVariables.TOKEN)
            val farmerId = if (farmerResult is Resource.Success) {
                farmerResult.data?.id ?: 0
            } else {
                0
            }
            state.value.data?.get(selectedDate.value)?.let { availableDate ->
                val appointment = Appointment(
                    id = 0,
                    advisorId = advisorId,
                    farmerId = farmerId,
                    message = comment.value,
                    status = "PENDING",
                    scheduledDate = availableDate.availableDate,
                    startTime = availableDate.startTime,
                    endTime = availableDate.endTime,
                    meetingUrl = ""
                )
                _state.value = UIState(isLoading = true)
                    val result = appointmentRepository.createAppointment(GlobalVariables.TOKEN, appointment)
                    if (result is Resource.Success) {
                        availableDateRepository.deleteAvailableDate(availableDate.id, GlobalVariables.TOKEN)
                        _comment.value = ""
                        _selectedDate.intValue = -1
                        _isExpanded.value = false
                        navController.navigate(Routes.NewAppointmentConfirmation.route)
                    } else {
                        _state.value = UIState(message = "Error al crear la cita")
                    }
            }
        }

    }

    fun setComment(comment: String) {
        _comment.value = comment
    }

    fun toggleExpanded() {
        _isExpanded.value = !_isExpanded.value
    }

    fun setSelectedDate(dateId: Int) {
        _selectedDate.intValue = dateId
    }
}