package com.example.agrotech.presentation.rating

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.agrotech.common.GlobalVariables
import com.example.agrotech.common.Resource
import com.example.agrotech.common.UIState
import com.example.agrotech.data.repository.advisor.AdvisorRepository
import com.example.agrotech.data.repository.appointment.AppointmentRepository
import com.example.agrotech.data.repository.appointment.AvailableDateRepository
import com.example.agrotech.data.repository.profile.ProfileRepository
import com.example.agrotech.data.repository.appointment.ReviewRepository
import com.example.agrotech.domain.appointment.Review
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FarmerReviewAppointmentViewModel @Inject constructor(
    private val reviewRepository: ReviewRepository,
    private val appointmentRepository: AppointmentRepository,
    private val availableDateRepository: AvailableDateRepository,
    private val advisorRepository: AdvisorRepository,
    private val profileRepository: ProfileRepository
) : ViewModel() {

    private val _state = mutableStateOf(UIState<Unit>())
    val state: State<UIState<Unit>> get() = _state

    private val _comment = mutableStateOf<String?>(null)
    val comment: State<String?> get() = _comment

    private val _rating = mutableStateOf<Int?>(null)
    val rating: State<Int?> get() = _rating

    private val _isSubmitting = mutableStateOf(false)
    val isSubmitting: State<Boolean> get() = _isSubmitting

    private val _hasReview = mutableStateOf(false)
    val hasReview: State<Boolean> get() = _hasReview

    private val _advisorName = mutableStateOf("")
    val advisorName: State<String> get() = _advisorName

    private val _advisorImage = mutableStateOf("")
    val advisorImage: State<String> get() = _advisorImage

    fun setComment(comment: String) {
        _comment.value = comment
    }

    fun setRating(rating: Int) {
        _rating.value = rating
    }

    fun loadAdvisorDetails(appointmentId: Long) {
        _comment.value = ""
        _rating.value = 0
        _hasReview.value = false

        viewModelScope.launch {
            val appointmentResult = appointmentRepository.getAppointmentById(appointmentId, GlobalVariables.TOKEN)

            if (appointmentResult is Resource.Success && appointmentResult.data != null) {
                val appointment = appointmentResult.data
                val availableDateResult = availableDateRepository.getAvailableDateById(appointmentResult.data.availableDateId, GlobalVariables.TOKEN)
                val advisorId = if (availableDateResult is Resource.Success) availableDateResult.data?.advisorId ?: 0 else 0
                val reviewResult = reviewRepository.getReviewByAdvisorIdAndFarmerId(advisorId, appointment.farmerId, GlobalVariables.TOKEN)
                if (reviewResult is Resource.Success && reviewResult.data != null) {
                    val review = reviewResult.data
                    _hasReview.value = true
                    _comment.value = review.comment
                    _rating.value = review.rating
                } else {
                    _comment.value = ""
                    _rating.value = 0
                    _hasReview.value = false
                }


                val advisorResult = advisorRepository.searchAdvisorByAdvisorId(advisorId, GlobalVariables.TOKEN)
                if (advisorResult is Resource.Success && advisorResult.data != null) {
                    val advisor = advisorResult.data
                    val profileResult = advisor.userId?.let { userId ->
                        profileRepository.searchProfile(userId, GlobalVariables.TOKEN)
                    }

                    if (profileResult is Resource.Success && profileResult.data != null) {
                        val profile = profileResult.data
                        _advisorName.value = "${profile.firstName} ${profile.lastName}"
                        _advisorImage.value = profile.photo ?: ""
                    } else {
                        _state.value = UIState(message = "Error al cargar los detalles del perfil del asesor.")
                    }
                } else {
                    _state.value = UIState(message = "Error al cargar los detalles del asesor.")
                }
            } else {
                _state.value = UIState(message = "Error al cargar los detalles de la cita.")
            }
        }
    }

    fun submitReview(appointmentId: Long, onSuccess: () -> Unit) {
        if (_rating.value == 0) {
            _state.value = UIState(message = "Por favor, selecciona una calificación.")
            return
        }

        _isSubmitting.value = true

        viewModelScope.launch {
            val appointmentResult = appointmentRepository.getAppointmentById(appointmentId, GlobalVariables.TOKEN)

            if (appointmentResult is Resource.Success) {
                val appointment = appointmentResult.data
                if (appointment == null) {
                    _isSubmitting.value = false
                    _state.value = UIState(message = "Error al obtener la cita. Por favor, intenta nuevamente.")
                    return@launch
                }

                val availableDateResult = availableDateRepository.getAvailableDateById(appointment.availableDateId, GlobalVariables.TOKEN)
                val advisorId = if (availableDateResult is Resource.Success) availableDateResult.data?.advisorId ?: 0 else 0
                val farmerId = appointment?.farmerId ?: 0L

                val review = Review(
                    id = 0,
                    advisorId = advisorId,
                    farmerId = farmerId,
                    comment = _comment.value ?: "",
                    rating = _rating.value ?: 0
                )

                if (_hasReview.value) {
                    val reviewResult = reviewRepository.getReviewByAdvisorIdAndFarmerId(advisorId, farmerId, GlobalVariables.TOKEN)
                    if (reviewResult is Resource.Success && reviewResult.data != null) {
                        val existingReview = reviewResult.data
                        val updatedReview = review.copy(id = existingReview.id)
                        val updateResult = reviewRepository.updateReview(GlobalVariables.TOKEN, updatedReview.id, updatedReview)
                        if (updateResult is Resource.Success) {
                            _isSubmitting.value = false
                            _state.value = UIState(message = "Calificación actualizada exitosamente.")
                        } else {
                            _isSubmitting.value = false
                            _state.value = UIState(message = "Error al actualizar la calificación. Por favor, intenta nuevamente.")
                        }
                        return@launch
                    }
                }

                val result = reviewRepository.createReview(GlobalVariables.TOKEN, review)
                _isSubmitting.value = false // Cambiar el estado para indicar que el envío ha finalizado

                if (result is Resource.Success) {
                    _state.value = UIState(message = "Calificación enviada exitosamente.")
                    onSuccess() // Regresar una vez que se envíe la reseña exitosamente
                } else {
                    _state.value = UIState(message = "Error al enviar la calificación. Por favor, intenta nuevamente.")
                }
            } else {
                _isSubmitting.value = false
                _state.value = UIState(message = "Error al obtener la cita. Por favor, intenta nuevamente.")
            }
        }
    }

    fun clearState() {
        _state.value = UIState()
    }
}