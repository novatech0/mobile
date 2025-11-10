package com.example.agrotech.presentation.welcomesection

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.agrotech.common.GlobalVariables
import com.example.agrotech.common.Resource
import com.example.agrotech.common.Routes
import com.example.agrotech.common.UIState
import com.example.agrotech.data.repository.advisor.AdvisorRepository
import com.example.agrotech.data.repository.authentication.AuthenticationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WelcomeViewModel @Inject constructor(
    private val authenticationRepository: AuthenticationRepository) : ViewModel() {

    private val _state = mutableStateOf(UIState<Unit>())
    val state: State<UIState<Unit>> get() = _state

    private val _navigationRoute = mutableStateOf<String?>(null)
    val navigationRoute: State<String?> get() = _navigationRoute

    fun checkUser() {
        _state.value = UIState(isLoading = true)
        viewModelScope.launch {
            when (val result = authenticationRepository.getUser()) {
                is Resource.Success -> {
                    GlobalVariables.TOKEN = result.data?.token ?: ""
                    GlobalVariables.USER_ID = result.data?.id ?: 0
                    when {
                        GlobalVariables.ROLES.contains("ROLE_ADVISOR") -> {
                            _navigationRoute.value = Routes.AdvisorHome.route
                        }
                        GlobalVariables.ROLES.contains("ROLE_FARMER") -> {
                            _navigationRoute.value = Routes.FarmerHome.route
                        }
                    }
                    _state.value = UIState(isLoading = false)
                }

                is Resource.Error -> {
                    _state.value = UIState(isLoading = false)
                }
            }
        }
    }
    fun clearNavigation() {
        _navigationRoute.value = null
    }
}
