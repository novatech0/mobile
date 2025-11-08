package com.example.agrotech.presentation.farmerappointmentdetail

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.agrotech.data.repository.advisor.AdvisorRepository
import com.example.agrotech.data.repository.appointment.AppointmentRepository
import com.example.agrotech.data.repository.profile.ProfileRepository
import com.example.agrotech.presentation.farmerhistory.AppointmentCard
import com.example.agrotech.common.Resource
import kotlinx.coroutines.launch
import com.example.agrotech.common.GlobalVariables
import com.example.agrotech.common.Routes
import com.example.agrotech.domain.appointment.Appointment
import androidx.compose.runtime.State
import com.example.agrotech.data.repository.appointment.AvailableDateRepository
import com.example.agrotech.data.repository.appointment.ReviewRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import javax.inject.Inject

@HiltViewModel
class FarmerAppointmentDetailViewModel @Inject constructor(
    private val appointmentRepository: AppointmentRepository,
    private val advisorRepository: AdvisorRepository,
    private val profileRepository: ProfileRepository,
    private val reviewRepository: ReviewRepository,
    private val availableDateRepository: AvailableDateRepository,
) : ViewModel() {

    private val _appointmentDetails = MutableLiveData<AppointmentCard?>()
    val appointmentDetails: LiveData<AppointmentCard?> get() = _appointmentDetails

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    private val _showCancelDialog = MutableLiveData<Boolean>()
    val showCancelDialog: LiveData<Boolean> get() = _showCancelDialog

    private val _appointmentStatusState = mutableStateOf<Appointment?>(null)
    val appointmentStatusState: State<Appointment?> get() = _appointmentStatusState

    private val _errorStateMessage = mutableStateOf<String?>(null)
    val errorStateMessage: State<String?> get() = _errorStateMessage

    private var pollingJob: Job? = null

    fun goBack() {
        _showCancelDialog.value = false
    }

    fun getStatusAppointment(
        appointmentId: Long,
        onNavigateToReview: ((Long) -> Unit)? = null,
        onNavigateToList: (() -> Unit)? = null
    ) {
        viewModelScope.launch {
            val appointmentResult = appointmentRepository.getAppointmentById(appointmentId, GlobalVariables.TOKEN)

            if (appointmentResult is Resource.Success && appointmentResult.data != null) {
                val appointment = appointmentResult.data
                _appointmentStatusState.value = appointment
                val availableDateResult = availableDateRepository.getAvailableDateById(appointment.availableDateId, GlobalVariables.TOKEN)
                val advisorId = if (availableDateResult is Resource.Success) availableDateResult.data?.advisorId ?: 0 else 0

                if (appointment.status == "COMPLETED") {
                    val reviewResult = reviewRepository.getReviewByAdvisorIdAndFarmerId(advisorId, appointment.farmerId, GlobalVariables.TOKEN)
                    if (reviewResult is Resource.Success && reviewResult.data != null) {
                        onNavigateToList?.invoke()
                    }
                    onNavigateToReview?.invoke(appointmentId)
                }
            } else {
                _errorStateMessage.value = "Error al cargar los detalles de la cita."
            }
        }
    }

    fun startPollingAppointmentStatus(appointmentId: Long) {
        pollingJob = viewModelScope.launch {
            while (true) {
                getStatusAppointment(appointmentId)
                delay(5000) // Revisar cada 5 segundos (ajusta el delay si es necesario)
            }
        }
    }

    fun stopPollingAppointmentStatus() {
        pollingJob?.cancel()
    }

    fun loadAppointmentDetails(appointmentId: Long) {
        _isLoading.value = true
        viewModelScope.launch {
            val appointmentResult = appointmentRepository.getAppointmentById(appointmentId, GlobalVariables.TOKEN)

            if (appointmentResult is Resource.Success && appointmentResult.data != null) {
                val appointment = appointmentResult.data

                val availableDateResult = availableDateRepository.getAvailableDateById(appointment.availableDateId, GlobalVariables.TOKEN)
                val advisorId = if (availableDateResult is Resource.Success) availableDateResult.data?.advisorId ?: 0 else 0
                val advisorResult = advisorRepository.searchAdvisorByAdvisorId(advisorId, GlobalVariables.TOKEN)
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

                _appointmentDetails.value = AppointmentCard(
                    id = appointment.id,
                    advisorName = advisorName,
                    advisorPhoto = advisorPhoto,
                    message = appointment.message,
                    status = appointment.status,
                    scheduledDate = availableDateResult.data?.scheduledDate ?: "Fecha no disponible",
                    startTime = availableDateResult.data?.startTime ?: "--:--",
                    endTime = availableDateResult.data?.endTime ?: "--:--",
                    meetingUrl = appointment.meetingUrl
                )

                _isLoading.value = false

            } else if (appointmentResult is Resource.Error) {
                _isLoading.value = false
                _errorMessage.value = "Error al obtener los detalles de la cita"
            }
        }
    }

    fun onCancelAppointmentClick() {
        _showCancelDialog.value = true
    }

    fun cancelAppointment(appointmentId: Long, cancelReason: String, onSuccess: () -> Unit) {
        _isLoading.value = true
        viewModelScope.launch {
            val result = appointmentRepository.deleteAppointment(appointmentId, GlobalVariables.TOKEN)
            if (result is Resource.Success) {
                onSuccess()
            } else if (result is Resource.Error) {
                _errorMessage.value = "Error al cancelar la cita"
            }
            _isLoading.value = false
        }

        onDismissCancelDialog()
    }

    fun onDismissCancelDialog() {
        _showCancelDialog.value = false
    }
}
