package com.example.agrotech.presentation.confirmcreationaccountfarmer

import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.agrotech.common.Routes

class ConfirmCreationAccountFarmerViewModel(private val navController: NavController) : ViewModel() {

    fun goToFarmerHomeScreen() {
        navController.navigate(Routes.FarmerHome.route)
    }

}