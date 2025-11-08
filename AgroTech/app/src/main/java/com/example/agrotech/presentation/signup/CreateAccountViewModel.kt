package com.example.agrotech.presentation.signup

import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.agrotech.common.GlobalVariables
import com.example.agrotech.common.Routes
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CreateAccountViewModel @Inject constructor() : ViewModel() {
    fun goToFormsFarmer() {
        GlobalVariables.ROLES = listOf("ROLE_USER", "ROLE_FARMER")
    }

    fun goToFormsAdvisor() {
        GlobalVariables.ROLES = listOf("ROLE_USER", "ROLE_ADVISOR")
    }
}
