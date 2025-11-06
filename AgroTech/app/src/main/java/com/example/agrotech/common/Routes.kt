package com.example.agrotech.common

sealed class Routes(val route: String) {
    // General
    data object Welcome : Routes("Welcome")
    data object SignIn : Routes("SignIn")
    data object SignUp : Routes("SignUp")
    data object NotificationList : Routes("NotificationList")
    // Farmer
    data object FarmerHome : Routes("FarmerHome")
    data object ReviewList : Routes("ReviewList")
    data object EnclosureList : Routes("EnclosureList")
    data object AnimalList : Routes("AnimalList")
    data object AnimalDetail : Routes("AnimalDetail")
    data object NewAppointment : Routes("NewAppointment")
    data object NewAppointmentConfirmation : Routes("NewAppointmentConfirmation")
    data object FarmerAppointmentList : Routes("FarmerAppointmentList")
    data object FarmerAppointmentHistory : Routes("FarmerAppointmentHistory")
    data object FarmerAppointmentDetail : Routes("FarmerAppointmentDetail")
    data object CancelAppointmentConfirmation : Routes("CancelAppointmentConfirmation")
    data object FarmerReviewAppointment : Routes("FarmerReviewAppointment")
    data object CreateAccountFarmer : Routes("CreateAccountFarmer")
    data object CreateProfileFarmer : Routes("CreateProfileFarmer")
    data object ConfirmCreationAccountFarmer : Routes("ConfirmCreationAccountFarmer")
    data object ExplorePosts : Routes("ExplorePosts")
    data object FarmerProfile : Routes("FarmerProfile")
    // Advisor
    data object AdvisorHome : Routes("AdvisorHome")
    data object AdvisorProfile : Routes("AdvisorProfile")
    data object AdvisorList : Routes("AdvisorList")
    data object AdvisorAvailableDates : Routes("AdvisorAvailableDates")
    data object AdvisorDetail : Routes("AdvisorDetail")
    data object AdvisorPosts : Routes("AdvisorPosts")
    data object AdvisorPostDetail : Routes("AdvisorPostDetail")
    data object NewPost : Routes("NewPost")
    data object CreateAccountAdvisor : Routes("CreateAccountAdvisor")
    data object CreateProfileAdvisor : Routes("CreateProfileAdvisor")
    data object ConfirmCreationAccountAdvisor : Routes("ConfirmCreationAccountAdvisor")
    data object ConfirmDeletionAppointmentAdvisor : Routes("ConfirmDeletionAppointmentAdvisor")
    data object AppointmentsAdvisorList : Routes("AppointmentsAdvisorList")
    data object AdvisorAppointmentDetail : Routes("AdvisorAppointmentDetail")
    data object AppointmentsAdvisorHistoryList : Routes("AppointmentsAdvisorHistoryList")

}