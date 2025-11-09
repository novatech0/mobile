package com.example.agrotech.presentation

import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.agrotech.common.Routes
import com.example.agrotech.presentation.advisorappointmentdetail.AdvisorAppointmentDetailScreen
import com.example.agrotech.presentation.advisorappointmentdetail.AdvisorAppointmentDetailViewModel
import com.example.agrotech.presentation.advisorappointmentdetail.CancelAppointmentAdvisorSuccessScreen
import com.example.agrotech.presentation.advisorappointments.AdvisorAppointmentsScreen
import com.example.agrotech.presentation.advisorappointments.AdvisorAppointmentsViewModel
import com.example.agrotech.presentation.advisoravailabledates.AdvisorAvailableDatesScreen
import com.example.agrotech.presentation.advisoravailabledates.AdvisorAvailableDatesViewModel
import com.example.agrotech.presentation.advisordetail.AdvisorDetailScreen
import com.example.agrotech.presentation.advisordetail.AdvisorDetailViewModel
import com.example.agrotech.presentation.advisorhistory.AdvisorAppointmentsHistoryScreen
import com.example.agrotech.presentation.advisorhome.AdvisorHomeScreen
import com.example.agrotech.presentation.advisorhome.AdvisorHomeViewModel
import com.example.agrotech.presentation.advisorlist.AdvisorListScreen
import com.example.agrotech.presentation.advisorlist.AdvisorListViewModel
import com.example.agrotech.presentation.advisorpostdetail.AdvisorPostDetailScreen
import com.example.agrotech.presentation.advisorpostdetail.AdvisorPostDetailViewModel
import com.example.agrotech.presentation.advisorposts.AdvisorPostsScreen
import com.example.agrotech.presentation.advisorposts.AdvisorPostsViewModel
import com.example.agrotech.presentation.advisorprofile.AdvisorProfileScreen
import com.example.agrotech.presentation.animaldetails.AnimalDetailsScreen
import com.example.agrotech.presentation.animaldetails.AnimalDetailsViewModel
import com.example.agrotech.presentation.animallist.AnimalListScreen
import com.example.agrotech.presentation.animallist.AnimalListViewModel
import com.example.agrotech.presentation.confirmcreationaccountadvisor.ConfirmCreationAccountAdvisorScreen
import com.example.agrotech.presentation.confirmcreationaccountfarmer.ConfirmCreationAccountFarmerScreen
import com.example.agrotech.presentation.createaccountadvisor.CreateAccountAdvisorScreen
import com.example.agrotech.presentation.createaccountadvisor.CreateAccountAdvisorViewModel
import com.example.agrotech.presentation.createaccountfarmer.CreateAccountFarmerScreen
import com.example.agrotech.presentation.createaccountfarmer.CreateAccountFarmerViewModel
import com.example.agrotech.presentation.createprofileadvisor.CreateProfileAdvisorScreen
import com.example.agrotech.presentation.createprofileadvisor.CreateProfileAdvisorViewModel
import com.example.agrotech.presentation.createprofilefarmer.CreateProfileFarmerScreen
import com.example.agrotech.presentation.enclosurelist.EnclosureListScreen
import com.example.agrotech.presentation.enclosurelist.EnclosureListViewModel
import com.example.agrotech.presentation.exploreposts.ExplorePostsScreen
import com.example.agrotech.presentation.exploreposts.ExplorePostsViewModel
import com.example.agrotech.presentation.farmerappointmentdetail.CancelAppointmentSuccessScreen
import com.example.agrotech.presentation.farmerappointmentdetail.FarmerAppointmentDetailScreen
import com.example.agrotech.presentation.farmerappointmentdetail.FarmerAppointmentDetailViewModel
import com.example.agrotech.presentation.farmerappointments.FarmerAppointmentListScreen
import com.example.agrotech.presentation.farmerappointments.FarmerAppointmentListViewModel
import com.example.agrotech.presentation.farmerhistory.FarmerAppointmentHistoryListScreen
import com.example.agrotech.presentation.farmerhistory.FarmerHistoryViewModel
import com.example.agrotech.presentation.farmerhome.FarmerHomeScreen
import com.example.agrotech.presentation.farmerhome.FarmerHomeViewModel
import com.example.agrotech.presentation.farmerprofile.FarmerProfileScreen
import com.example.agrotech.presentation.farmerprofile.FarmerProfileViewModel
import com.example.agrotech.presentation.login.LoginScreen
import com.example.agrotech.presentation.login.LoginViewModel
import com.example.agrotech.presentation.newappointment.NewAppointmentScreen
import com.example.agrotech.presentation.newappointment.NewAppointmentSuccessScreen
import com.example.agrotech.presentation.newappointment.NewAppointmentViewModel
import com.example.agrotech.presentation.newpost.NewPostScreen
import com.example.agrotech.presentation.newpost.NewPostViewModel
import com.example.agrotech.presentation.notificationlist.NotificationListScreen
import com.example.agrotech.presentation.notificationlist.NotificationListViewModel
import com.example.agrotech.presentation.rating.FarmerReviewAppointmentScreen
import com.example.agrotech.presentation.rating.FarmerReviewAppointmentViewModel
import com.example.agrotech.presentation.reviewlist.ReviewListScreen
import com.example.agrotech.presentation.reviewlist.ReviewListViewModel
import com.example.agrotech.presentation.signup.CreateAccountScreen
import com.example.agrotech.presentation.signup.CreateAccountViewModel
import com.example.agrotech.presentation.welcomesection.WelcomeScreen
import com.example.agrotech.presentation.welcomesection.WelcomeViewModel

@Composable
fun AppNavigation(navController: androidx.navigation.NavHostController) {
    NavHost(navController = navController, startDestination = Routes.Welcome.route) {
        // --- Pantallas principales ---
        composable(Routes.Welcome.route) {
            val vm = hiltViewModel<WelcomeViewModel>()
            WelcomeScreen(navController, vm)
        }

        composable(Routes.SignIn.route) {
            val vm = hiltViewModel<LoginViewModel>()
            LoginScreen(navController, vm)
        }

        composable(Routes.FarmerHome.route) {
            val vm = hiltViewModel<FarmerHomeViewModel>()
            FarmerHomeScreen(navController, vm)
        }

        composable(Routes.AdvisorHome.route) {
            val vm = hiltViewModel<AdvisorHomeViewModel>()
            AdvisorHomeScreen(navController, vm)
        }

        // --- Citas y disponibilidad ---
        composable(Routes.AdvisorAvailableDates.route) {
            val vm = hiltViewModel<AdvisorAvailableDatesViewModel>()
            AdvisorAvailableDatesScreen(navController, vm)
        }

        composable(Routes.FarmerAppointmentList.route) {
            val vm = hiltViewModel<FarmerAppointmentListViewModel>()
            FarmerAppointmentListScreen(navController, vm)
        }

        composable(Routes.FarmerAppointmentHistory.route) {
            val vm = hiltViewModel<FarmerHistoryViewModel>()
            FarmerAppointmentHistoryListScreen(navController, vm)
        }

        composable(Routes.FarmerAppointmentDetail.route + "/{appointmentId}") {
            val vm = hiltViewModel<FarmerAppointmentDetailViewModel>()
            val id = it.arguments?.getString("appointmentId")?.toLong() ?: 0
            FarmerAppointmentDetailScreen(navController, vm, id)
        }

        composable(Routes.NewAppointment.route + "/{advisorId}") {
            val vm = hiltViewModel<NewAppointmentViewModel>()
            val id = it.arguments?.getString("advisorId")?.toLong() ?: 0
            NewAppointmentScreen(navController, vm, id)
        }

        composable(Routes.NewAppointmentConfirmation.route) {
            NewAppointmentSuccessScreen() {
                navController.navigate(Routes.FarmerAppointmentList.route)
            }
        }

        composable(Routes.FarmerReviewAppointment.route + "/{appointmentId}") {
            val vm = hiltViewModel<FarmerReviewAppointmentViewModel>()
            val id = it.arguments?.getString("appointmentId")?.toLong() ?: 0
            FarmerReviewAppointmentScreen(navController, vm, id)
        }

        composable(Routes.AppointmentsAdvisorList.route) {
            val vm = hiltViewModel<AdvisorAppointmentsViewModel>()
            AdvisorAppointmentsScreen(navController, vm)
        }

        composable(Routes.AppointmentsAdvisorHistoryList.route) {
            val vm = hiltViewModel<AdvisorAppointmentsViewModel>()
            AdvisorAppointmentsHistoryScreen(navController, vm)
        }

        composable(Routes.AdvisorAppointmentDetail.route + "/{appointmentId}") {
            val vm = hiltViewModel<AdvisorAppointmentDetailViewModel>()
            val id = it.arguments?.getString("appointmentId")?.toLong() ?: 0
            AdvisorAppointmentDetailScreen(navController, id, vm)
        }

        // --- Publicaciones ---
        composable(Routes.ExplorePosts.route) {
            val vm = hiltViewModel<ExplorePostsViewModel>()
            ExplorePostsScreen(navController, vm)
        }

        composable(Routes.AdvisorPosts.route) {
            val vm = hiltViewModel<AdvisorPostsViewModel>()
            AdvisorPostsScreen(navController, vm)
        }

        composable(Routes.AdvisorPostDetail.route + "/{postId}") {
            val vm = hiltViewModel<AdvisorPostDetailViewModel>()
            val id = it.arguments?.getString("postId")?.toLong() ?: 0
            AdvisorPostDetailScreen(navController, vm, id)
        }

        composable(Routes.NewPost.route) {
            val vm = hiltViewModel<NewPostViewModel>()
            NewPostScreen(navController, vm)
        }

        // --- Perfiles ---
        composable(Routes.FarmerProfile.route) {
            val vm = hiltViewModel<FarmerProfileViewModel>()
            FarmerProfileScreen(navController, vm)
        }

        composable(Routes.AdvisorProfile.route) {
            val vm = hiltViewModel<com.example.agrotech.presentation.advisorprofile.AdvisorProfileViewModel>()
            AdvisorProfileScreen(navController, vm)
        }

        // --- Creación de cuenta ---
        composable(Routes.SignUp.route) {
            val vm = hiltViewModel<CreateAccountViewModel>()
            CreateAccountScreen(navController, vm)
        }

        composable(Routes.CreateAccountAdvisor.route) {
            val vm = hiltViewModel<CreateAccountAdvisorViewModel>()
            CreateAccountAdvisorScreen(navController, vm)
        }

        composable(Routes.CreateAccountFarmer.route) {
            val vm = hiltViewModel<CreateAccountFarmerViewModel>()
            CreateAccountFarmerScreen(navController, vm)
        }

        composable(Routes.CreateProfileAdvisor.route) {
            val vm = hiltViewModel<CreateProfileAdvisorViewModel>()
            CreateProfileAdvisorScreen(navController, vm)
        }

        composable(Routes.CreateProfileFarmer.route) {
            val vm = hiltViewModel<com.example.agrotech.presentation.createprofilefarmer.CreateProfileFarmerViewModel>()
            CreateProfileFarmerScreen(navController, vm)
        }

        composable(Routes.ConfirmCreationAccountAdvisor.route) {
            ConfirmCreationAccountAdvisorScreen(navController)
        }

        composable(Routes.ConfirmCreationAccountFarmer.route) {
            ConfirmCreationAccountFarmerScreen(navController)
        }

        // --- Cancelaciones ---
        composable(Routes.CancelAppointmentConfirmation.route) {
            CancelAppointmentSuccessScreen() {
                navController.navigate(Routes.FarmerAppointmentList.route)
            }
        }

        composable(Routes.ConfirmDeletionAppointmentAdvisor.route) {
            val vm = hiltViewModel<AdvisorAppointmentDetailViewModel>()
            CancelAppointmentAdvisorSuccessScreen(navController, vm)
        }

        // --- Asesores y reseñas ---
        composable(Routes.AdvisorList.route) {
            val vm = hiltViewModel<AdvisorListViewModel>()
            AdvisorListScreen(navController, vm)
        }

        composable(Routes.AdvisorDetail.route + "/{advisorId}") { backStackEntry ->
            val vm = hiltViewModel<AdvisorDetailViewModel>()
            val id = backStackEntry.arguments?.getString("advisorId")?.toLong() ?: 0
            AdvisorDetailScreen(navController, vm, id)
        }

        composable(Routes.ReviewList.route + "/{advisorId}") {
            val vm = hiltViewModel<ReviewListViewModel>()
            val id = it.arguments?.getString("advisorId")?.toLong() ?: 0
            ReviewListScreen(navController, vm, id)
        }

        // --- Notificaciones ---
        composable(Routes.NotificationList.route) {
            val vm = hiltViewModel<NotificationListViewModel>()
            NotificationListScreen(navController, vm)
        }

        // --- Animales y recintos ---
        composable(Routes.EnclosureList.route) {
            val vm = hiltViewModel<EnclosureListViewModel>()
            EnclosureListScreen(navController, vm)
        }

        composable(Routes.AnimalList.route + "/{enclosureId}") {
            val vm = hiltViewModel<AnimalListViewModel>()
            val id = it.arguments?.getString("enclosureId")?.toLong() ?: 0
            AnimalListScreen(navController, vm, id)
        }

        composable(Routes.AnimalDetail.route + "/{animalId}") {
            val vm = hiltViewModel<AnimalDetailsViewModel>()
            val id = it.arguments?.getString("animalId")?.toLong() ?: 0
            AnimalDetailsScreen(navController, vm, id)
        }
    }
}