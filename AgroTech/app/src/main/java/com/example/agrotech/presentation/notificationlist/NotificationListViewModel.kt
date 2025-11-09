package com.example.agrotech.presentation.notificationlist

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.agrotech.common.GlobalVariables
import com.example.agrotech.common.Resource
import com.example.agrotech.common.UIState
import com.example.agrotech.data.repository.notification.NotificationRepository
import com.example.agrotech.domain.notification.Notification
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationListViewModel @Inject constructor(
    private val notificationRepository: NotificationRepository) : ViewModel() {

    private val _state = mutableStateOf(UIState<List<Notification>>())
    val state: State<UIState<List<Notification>>> get() = _state

    fun getNotificationList() {
        _state.value = UIState(isLoading = true)
        viewModelScope.launch {
            val result = notificationRepository.getNotifications(GlobalVariables.USER_ID, GlobalVariables.TOKEN)
            if (result is Resource.Success) {
                val notifications = result.data?.sortedByDescending { it.sendAt } ?: run {
                    _state.value = UIState(message = "No se encontraron notificaciones")
                    return@launch
                }
                _state.value = UIState(data = notifications)
            } else {
                _state.value = UIState(message = "Error al intentar obtener las notificaciones")
            }
        }
    }

    fun deleteNotification(notificationId: Long) {
        viewModelScope.launch {
            val result = notificationRepository.deleteNotification(notificationId, GlobalVariables.TOKEN)
            if (result is Resource.Success) {
                val notifications = _state.value.data?.toMutableList() ?: return@launch
                notifications.removeIf { it.id == notificationId }
                _state.value = UIState(data = notifications)
            } else {
                _state.value = UIState(message = "Error al intentar eliminar la notificación")
            }
        }
    }
}