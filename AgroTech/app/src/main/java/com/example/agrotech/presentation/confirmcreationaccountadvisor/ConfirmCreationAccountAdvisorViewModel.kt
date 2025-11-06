package com.example.agrotech.presentation.confirmcreationaccountadvisor

import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.agrotech.common.Routes

class ConfirmCreationAccountAdvisorViewModel(private val navController: NavController) : ViewModel() {

    fun goToAdvisorHomeScreen() {
        navController.navigate(Routes.AdvisorHome.route)
    }

}