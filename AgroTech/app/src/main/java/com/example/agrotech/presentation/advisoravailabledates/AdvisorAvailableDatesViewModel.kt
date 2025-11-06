package com.example.agrotech.presentation.advisoravailabledates

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.agrotech.data.repository.appointment.AvailableDateRepository
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.agrotech.common.GlobalVariables
import com.example.agrotech.common.Resource
import com.example.agrotech.common.Routes
import com.example.agrotech.data.repository.advisor.AdvisorRepository
import com.example.agrotech.data.repository.authentication.AuthenticationRepository
import com.example.agrotech.domain.appointment.AvailableDate
import com.example.agrotech.domain.authentication.AuthenticationResponse
import kotlinx.coroutines.launch


class AdvisorAvailableDatesViewModel(
    private val navController: NavController,
    private val availableDateRepository: AvailableDateRepository,
    private val advisorRepository: AdvisorRepository,
    private val authenticationRepository: AuthenticationRepository,
) : ViewModel() {

    private val _availableDates = MutableLiveData<List<AvailableDate>?>()
    val availableDates: LiveData<List<AvailableDate>?> get() = _availableDates

    private val _expanded = mutableStateOf(false)
    val expanded: State<Boolean> get() = _expanded

    var advisorId: Long? = null
        private set

    suspend fun initScreen() {
        fetchAdvisorIdAndDates()
    }

    fun deleteAvailableDate(availableDateId: Long) {
        viewModelScope.launch {
            try {
                val response = availableDateRepository.deleteAvailableDate(availableDateId, GlobalVariables.TOKEN)
                if (response is Resource.Success) {
                    fetchAvailableDates()
                } else {
                    println("Error deleting available date: ${response.message}")
                }
            } catch (e: Exception) {
                println("Error deleting available date: ${e.message}")
            }
        }
    }

    private suspend fun fetchAdvisorIdAndDates() {
        try {
            val advisorResult = advisorRepository.searchAdvisorByUserId(GlobalVariables.USER_ID, GlobalVariables.TOKEN)
            if (advisorResult is Resource.Success) {
                advisorId = advisorResult.data?.id
                fetchAvailableDates()
            } else {
                println("Error fetching advisor ID: ${advisorResult.message}")
            }
        } catch (e: Exception) {
            println("Error fetching advisor ID: ${e.message}")
        }
    }

    fun fetchAvailableDates() {
        viewModelScope.launch {
            try {
                val id = advisorId ?: return@launch
                val result = availableDateRepository.getAvailableDatesByAdvisor(id, GlobalVariables.TOKEN)
                if (result is Resource.Success) {
                    _availableDates.value = result.data
                } else {
                    println("Error fetching available dates: ${result.message}")
                }
            } catch (e: Exception) {
                println("Error fetching available dates: ${e.message}")
            }
        }
    }
    fun setExpanded(value: Boolean) {
        _expanded.value = value
    }
    fun goBack() {
        navController.popBackStack()
    }
    private fun goToWelcomeSection() {
        navController.navigate(Routes.Welcome.route)
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

    fun createAvailableDate(createAvailableDate: AvailableDate) {
        viewModelScope.launch {
            try {
                val response = availableDateRepository.createAvailableDate(GlobalVariables.TOKEN, createAvailableDate)
                if (response is Resource.Success) {
                    fetchAvailableDates()
                } else {
                    println("Error creating available date: ${response.message}")
                }
            } catch (e: Exception) {
                println("Error creating available date: ${e.message}")
            }
        }
    }

    fun goToProfile() {
        navController.navigate(Routes.AdvisorProfile.route)
    }
}
