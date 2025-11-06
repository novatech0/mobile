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
import com.example.agrotech.data.repository.notification.NotificationRepository
import com.example.agrotech.domain.appointment.AvailableDate
import com.example.agrotech.domain.notification.Notification
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import java.util.Date
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class FarmerAppointmentDetailViewModel(
    private val navController: NavController,
    private val appointmentRepository: AppointmentRepository,
    private val advisorRepository: AdvisorRepository,
    private val profileRepository: ProfileRepository,
    private val reviewRepository: ReviewRepository,
    private val availableDateRepository: AvailableDateRepository,
    private val notificationRepository: NotificationRepository
) : ViewModel() {

    private val _appointmentDetails = MutableLiveData<AppointmentCard?>()
    val appointmentDetails: LiveData<AppointmentCard?> get() = _appointmentDetails

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    private val _isCancelled = MutableLiveData<Boolean>()
    val isCancelled: LiveData<Boolean> get() = _isCancelled

    private val _showCancelDialog = MutableLiveData<Boolean>()
    val showCancelDialog: LiveData<Boolean> get() = _showCancelDialog

    private val _appointmentStatusState = mutableStateOf<Appointment?>(null)
    val appointmentStatusState: State<Appointment?> get() = _appointmentStatusState

    private val _errorStateMessage = mutableStateOf<String?>(null)
    val errorStateMessage: State<String?> get() = _errorStateMessage

    private var pollingJob: Job? = null

    fun goBack() {
        _showCancelDialog.value = false
        navController.popBackStack()
    }

    fun getStatusAppointment(appointmentId: Long) {
        viewModelScope.launch {
            val appointmentResult = appointmentRepository.getAppointmentById(appointmentId, GlobalVariables.TOKEN)

            if (appointmentResult is Resource.Success && appointmentResult.data != null) {
                val appointment = appointmentResult.data
                _appointmentStatusState.value = appointment

                if (appointment.status == "COMPLETED") {
                    val reviewResult = reviewRepository.getReviewByAdvisorIdAndFarmerId(appointment.advisorId, appointment.farmerId, GlobalVariables.TOKEN)
                    if (reviewResult is Resource.Success && reviewResult.data != null) {
                        navController.navigate(Routes.FarmerAppointmentList.route)
                    }
                    navController.navigate(Routes.FarmerReviewAppointment.route + "/$appointmentId")
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

                _appointmentDetails.value = AppointmentCard(
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

    fun cancelAppointment(appointmentId: Long, cancelReason: String) {
        _isLoading.value = true
        viewModelScope.launch {
            // crear nuevamente el available date
            val appointmentResult = appointmentRepository.getAppointmentById(appointmentId, GlobalVariables.TOKEN)
            val availableDate = appointmentResult.data?.let {
                AvailableDate(
                    id = 0,
                    advisorId = it.advisorId,
                    availableDate = it.scheduledDate,
                    startTime = it.startTime,
                    endTime = it.endTime
                )
            }

            val result = appointmentRepository.deleteAppointment(appointmentId, GlobalVariables.TOKEN)

            if (result is Resource.Success) {
                _isCancelled.value = true
                val availableDateResult = availableDateRepository.createAvailableDate(GlobalVariables.TOKEN, availableDate!!)
                if (availableDateResult is Resource.Success) {


                    //

                    val currentDateTime = Date()
                    val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.getDefault())
                    formatter.timeZone = TimeZone.getTimeZone("America/Lima")
                    val formattedDateTime = formatter.format(currentDateTime)

                    val notification = appointmentResult.data?.let {
                        Notification(
                            id = 0,
                            userId = GlobalVariables.USER_ID,
                            title = "Cita cancelada",
                            message = "La cita programada para el ${it.scheduledDate} a las ${it.startTime} ha sido cancelada. Motivo: $cancelReason",
                            sendAt = formattedDateTime
                        )
                    }



                    notificationRepository.createNotification(notification!!, GlobalVariables.TOKEN)

                    /*

                    val advisorResult = advisorRepository.searchAdvisorByAdvisorId(appointmentResult.data?.advisorId!!, GlobalVariables.TOKEN)

                    val notificationAdvisor = appointmentResult.data?.let {
                        Notification(
                            id = 0,
                            userId = advisorResult.data?.userId!!,
                            title = "Cita cancelada",
                            message = "La cita programada para el ${it.scheduledDate} a las ${it.startTime} ha sido cancelada por el agricultor. Motivo: $cancelReason",
                            sendAt = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date()).toString()
                        )
                    }

                    notificationRepository.createNotification(notificationAdvisor!!, GlobalVariables.TOKEN)

                    * */


                    navController.navigate(Routes.CancelAppointmentConfirmation.route)
                }

            } else if (result is Resource.Error) {
                _errorMessage.value = "Error al cancelar la cita"
            }

        }

        onDismissCancelDialog()
    }

    fun onDismissCancelDialog() {
        _showCancelDialog.value = false
    }




}
