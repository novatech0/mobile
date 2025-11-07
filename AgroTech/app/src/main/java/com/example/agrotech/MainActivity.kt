package com.example.agrotech

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.example.agrotech.common.Constants
import com.example.agrotech.common.Routes
import com.example.agrotech.data.local.AppDatabase
import com.example.agrotech.data.remote.advisor.AdvisorService
import com.example.agrotech.data.remote.animal.AnimalService
import com.example.agrotech.data.remote.appointment.AppointmentService
import com.example.agrotech.data.remote.appointment.AvailableDateService
import com.example.agrotech.data.remote.appointment.ReviewService
import com.example.agrotech.data.remote.authentication.AuthenticationService
import com.example.agrotech.data.remote.enclosure.EnclosureService
import com.example.agrotech.data.remote.farmer.FarmerService
import com.example.agrotech.data.remote.notification.NotificationService
import com.example.agrotech.data.remote.post.PostService
import com.example.agrotech.data.remote.profile.ProfileService
import com.example.agrotech.data.repository.advisor.AdvisorRepository
import com.example.agrotech.data.repository.animal.AnimalRepository
import com.example.agrotech.data.repository.appointment.AppointmentRepository
import com.example.agrotech.data.repository.appointment.AvailableDateRepository
import com.example.agrotech.data.repository.appointment.ReviewRepository
import com.example.agrotech.data.repository.authentication.AuthenticationRepository
import com.example.agrotech.data.repository.enclosure.EnclosureRepository
import com.example.agrotech.data.repository.farmer.FarmerRepository
import com.example.agrotech.data.repository.notification.NotificationRepository
import com.example.agrotech.data.repository.post.PostRepository
import com.example.agrotech.data.repository.profile.ProfileRepository
import com.example.agrotech.presentation.advisorappointmentdetail.AdvisorAppointmentDetailScreen
import com.example.agrotech.presentation.advisorappointmentdetail.AdvisorAppointmentDetailViewModel
import com.example.agrotech.presentation.advisorappointmentdetail.CancelAppointmentAdvisorSuccessScreen
import com.example.agrotech.presentation.advisorhistory.AdvisorAppointmentsHistoryScreen
import com.example.agrotech.presentation.advisorappointments.AdvisorAppointmentsScreen
import com.example.agrotech.presentation.advisorappointments.AdvisorAppointmentsViewModel
import com.example.agrotech.presentation.advisoravailabledates.AdvisorAvailableDatesScreen
import com.example.agrotech.presentation.advisoravailabledates.AdvisorAvailableDatesViewModel
import com.example.agrotech.presentation.advisordetail.AdvisorDetailScreen
import com.example.agrotech.presentation.advisordetail.AdvisorDetailViewModel
import com.example.agrotech.presentation.advisorhome.AdvisorHomeScreen
import com.example.agrotech.presentation.advisorhome.AdvisorHomeViewModel
import com.example.agrotech.presentation.advisorlist.AdvisorListScreen
import com.example.agrotech.presentation.advisorlist.AdvisorListViewModel
import com.example.agrotech.presentation.advisorpostdetail.AdvisorPostDetailScreen
import com.example.agrotech.presentation.advisorpostdetail.AdvisorPostDetailViewModel
import com.example.agrotech.presentation.advisorposts.AdvisorPostsScreen
import com.example.agrotech.presentation.advisorposts.AdvisorPostsViewModel
import com.example.agrotech.presentation.advisorprofile.AdvisorProfileScreen
import com.example.agrotech.presentation.advisorprofile.AdvisorProfileViewModel
import com.example.agrotech.presentation.animaldetails.AnimalDetailsScreen
import com.example.agrotech.presentation.animaldetails.AnimalDetailsViewModel
import com.example.agrotech.presentation.animallist.AnimalListScreen
import com.example.agrotech.presentation.animallist.AnimalListViewModel
import com.example.agrotech.presentation.confirmcreationaccountadvisor.ConfirmCreationAccountAdvisorScreen
import com.example.agrotech.presentation.confirmcreationaccountadvisor.ConfirmCreationAccountAdvisorViewModel
import com.example.agrotech.presentation.farmerappointmentdetail.CancelAppointmentSuccessScreen
import com.example.agrotech.presentation.farmerappointmentdetail.FarmerAppointmentDetailScreen
import com.example.agrotech.presentation.farmerappointmentdetail.FarmerAppointmentDetailViewModel
import com.example.agrotech.presentation.confirmcreationaccountfarmer.ConfirmCreationAccountFarmerScreen
import com.example.agrotech.presentation.confirmcreationaccountfarmer.ConfirmCreationAccountFarmerViewModel
import com.example.agrotech.presentation.createaccountadvisor.CreateAccountAdvisorScreen
import com.example.agrotech.presentation.createaccountadvisor.CreateAccountAdvisorViewModel
import com.example.agrotech.presentation.createaccountfarmer.CreateAccountFarmerScreen
import com.example.agrotech.presentation.createaccountfarmer.CreateAccountFarmerViewModel
import com.example.agrotech.presentation.createprofileadvisor.CreateProfileAdvisorScreen
import com.example.agrotech.presentation.createprofileadvisor.CreateProfileAdvisorViewModel
import com.example.agrotech.presentation.createprofilefarmer.CreateProfileFarmerScreen
import com.example.agrotech.presentation.createprofilefarmer.CreateProfileFarmerViewModel
import com.example.agrotech.presentation.enclosurelist.EnclosureListScreen
import com.example.agrotech.presentation.enclosurelist.EnclosureListViewModel
import com.example.agrotech.presentation.exploreposts.ExplorePostsScreen
import com.example.agrotech.presentation.exploreposts.ExplorePostsViewModel
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
import com.example.agrotech.ui.theme.AgroTechTheme
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val retrofit = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val userDao = Room
            .databaseBuilder(applicationContext, AppDatabase::class.java, "agrotech-db")
            .build()
            .getUserDao()


        //Services
        val profileService = retrofit.create(ProfileService::class.java)
        val advisorService = retrofit.create(AdvisorService::class.java)
        val farmerService = retrofit.create(FarmerService::class.java)
        val reviewService = retrofit.create(ReviewService::class.java)
        val appointmentService = retrofit.create(AppointmentService::class.java)
        val availableDateService = retrofit.create(AvailableDateService::class.java)
        val notificationService = retrofit.create(NotificationService::class.java)
        val postService = retrofit.create(PostService::class.java)
        val authenticationService = retrofit.create(AuthenticationService::class.java)
        val enclosureService = retrofit.create(EnclosureService::class.java)
        val animalService = retrofit.create(AnimalService::class.java)

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AgroTechTheme {
                val navController = rememberNavController()
                //Repositories
                val profileRepository = ProfileRepository(profileService)
                val advisorRepository = AdvisorRepository(advisorService)
                val farmerRepository = FarmerRepository(farmerService)
                val appointmentRepository = AppointmentRepository(appointmentService)
                val availableDateRepository = AvailableDateRepository(availableDateService)
                val reviewRepository = ReviewRepository(reviewService)
                val notificationRepository = NotificationRepository(notificationService)
                val postRepository = PostRepository(postService)
                val authenticationRepository = AuthenticationRepository(authenticationService, userDao)
                val enclosureRepository = EnclosureRepository(enclosureService)
                val animalRepository = AnimalRepository(animalService)

                // View Models
                val welcomeViewModel = WelcomeViewModel(navController, authenticationRepository, advisorRepository)
                val loginViewModel = LoginViewModel(navController, authenticationRepository, advisorRepository)
                val farmerHomeViewModel = FarmerHomeViewModel(navController, profileRepository, authenticationRepository, appointmentRepository, farmerRepository, advisorRepository, notificationRepository)
                val advisorHomeViewModel = AdvisorHomeViewModel(navController, advisorRepository, appointmentRepository, profileRepository, farmerRepository, authenticationRepository, notificationRepository)
                val advisorAvailableDatesViewModel = AdvisorAvailableDatesViewModel(navController, availableDateRepository, advisorRepository, authenticationRepository)
                val advisorListViewModel = AdvisorListViewModel(navController, profileRepository, advisorRepository)
                val advisorDetailViewModel = AdvisorDetailViewModel(navController, profileRepository, advisorRepository, availableDateRepository)
                val advisorAppointmentDetailViewModel = AdvisorAppointmentDetailViewModel(navController, appointmentRepository, profileRepository, farmerRepository, reviewRepository, authenticationRepository)
                val advisorAppointmentsViewModel = AdvisorAppointmentsViewModel(navController, authenticationRepository, advisorRepository, appointmentRepository, profileRepository, farmerRepository)
                val reviewListViewModel = ReviewListViewModel(navController, reviewRepository, profileRepository, farmerRepository, advisorRepository)
                val newAppointmentViewModel = NewAppointmentViewModel(navController, availableDateRepository, appointmentRepository, farmerRepository)
                val farmerAppointmentListViewModel = FarmerAppointmentListViewModel(navController, profileRepository, advisorRepository, appointmentRepository, farmerRepository)
                val farmerHistoryViewModel = FarmerHistoryViewModel(navController, profileRepository, advisorRepository, appointmentRepository, farmerRepository)
                val farmerAppointmentDetailViewModel = FarmerAppointmentDetailViewModel(navController, appointmentRepository, advisorRepository, profileRepository, reviewRepository, availableDateRepository, notificationRepository)
                val farmerReviewAdvisorViewModel = FarmerReviewAppointmentViewModel(navController, reviewRepository, appointmentRepository, advisorRepository, profileRepository)
                val createAccountViewModel = CreateAccountViewModel(navController)
                val createAccountAdvisorViewModel = CreateAccountAdvisorViewModel(navController, authenticationRepository)
                val createAccountFarmerViewModel = CreateAccountFarmerViewModel(navController, authenticationRepository)
                val createProfileAdvisorViewModel = CreateProfileAdvisorViewModel(navController , profileRepository, createAccountAdvisorViewModel)
                val createProfileFarmerViewModel = CreateProfileFarmerViewModel(navController, profileRepository, createAccountFarmerViewModel)
                val confirmCreationAccountAdvisorViewModel = ConfirmCreationAccountAdvisorViewModel(navController)
                val confirmCreationAccountFarmerViewModel = ConfirmCreationAccountFarmerViewModel(navController)
                val notificationListViewModel = NotificationListViewModel(navController, notificationRepository)
                val explorePostsViewModel = ExplorePostsViewModel(navController, postRepository, profileRepository, advisorRepository)
                val farmerProfileViewModel = FarmerProfileViewModel(navController, profileRepository)
                val advisorProfileViewModel = AdvisorProfileViewModel(navController, profileRepository)
                val advisorPostsViewModel = AdvisorPostsViewModel(navController, postRepository, advisorRepository)
                val advisorPostDetailViewModel = AdvisorPostDetailViewModel(navController, postRepository)
                val newPostViewModel = NewPostViewModel(navController, postRepository, advisorRepository)
                val enclosureListViewModel = EnclosureListViewModel(navController, enclosureRepository, farmerRepository)
                val animalListViewModel = AnimalListViewModel(navController, animalRepository, enclosureRepository)
                val animalDetailsViewModel = AnimalDetailsViewModel(navController, animalRepository)
                //Navigation
                NavHost(navController = navController, startDestination = Routes.Welcome.route) {
                    composable(route = Routes.Welcome.route) {
                        WelcomeScreen(viewModel = welcomeViewModel)
                    }
                    composable(route = Routes.SignIn.route) {
                        LoginScreen(viewModel = loginViewModel)
                    }
                    composable(route = Routes.FarmerHome.route) {
                        FarmerHomeScreen(viewModel = farmerHomeViewModel)
                    }
                    composable(route = Routes.AdvisorHome.route) {
                        AdvisorHomeScreen(viewModel = advisorHomeViewModel)
                    }
                    composable(route = Routes.AdvisorAvailableDates.route) {
                        AdvisorAvailableDatesScreen(viewModel = advisorAvailableDatesViewModel)
                    }
                    composable(route = Routes.AdvisorList.route) {
                        AdvisorListScreen(viewModel = advisorListViewModel)
                    }
                    composable(route = Routes.AdvisorDetail.route + "/{advisorId}") { backStackEntry ->
                        val advisorId = backStackEntry.arguments?.getString("advisorId")?.toLong() ?: 0
                        AdvisorDetailScreen(viewModel = advisorDetailViewModel, advisorId = advisorId)
                    }
                    composable(route = Routes.ReviewList.route + "/{advisorId}") {
                        val advisorId = it.arguments?.getString("advisorId")?.toLong() ?: 0
                        ReviewListScreen(viewModel = reviewListViewModel, advisorId = advisorId)
                    }
                    composable(route = Routes.NewAppointment.route + "/{advisorId}") {
                        val advisorId = it.arguments?.getString("advisorId")?.toLong() ?: 0
                        NewAppointmentScreen(viewModel = newAppointmentViewModel, advisorId = advisorId)
                    }
                    composable(route = Routes.NewAppointmentConfirmation.route) {
                        NewAppointmentSuccessScreen {
                            navController.navigate(Routes.FarmerAppointmentList.route)
                        }
                    }
                    composable(route = Routes.AppointmentsAdvisorList.route) {
                        AdvisorAppointmentsScreen(viewModel = advisorAppointmentsViewModel)
                    }
                    composable(route = Routes.AdvisorAppointmentDetail.route + "/{appointmentId}") {
                        val appointmentId = it.arguments?.getString("appointmentId")?.toLong() ?: 0
                        AdvisorAppointmentDetailScreen(viewModel = advisorAppointmentDetailViewModel, appointmentId = appointmentId)

                    }
                    composable(route = Routes.AppointmentsAdvisorHistoryList.route) {
                        AdvisorAppointmentsHistoryScreen(viewModel = advisorAppointmentsViewModel)
                    }
                    composable(route = Routes.EnclosureList.route) {
                        EnclosureListScreen(viewModel = enclosureListViewModel)
                    }
                    composable(route = Routes.AnimalList.route + "/{enclosureId}") {
                        val enclosureId = it.arguments?.getString("enclosureId")?.toLong() ?: 0
                        AnimalListScreen(viewModel = animalListViewModel, enclosureId = enclosureId)

                    }
                    composable(route = Routes.AnimalDetail.route + "/{animalId}") {
                        val animalId = it.arguments?.getString("animalId")?.toLong() ?: 0
                        AnimalDetailsScreen(viewModel = animalDetailsViewModel, animalId = animalId)
                    }
                    composable(route = Routes.FarmerAppointmentList.route) {
                        FarmerAppointmentListScreen(viewModel = farmerAppointmentListViewModel)
                    }
                    composable(route = Routes.FarmerAppointmentHistory.route) {
                        FarmerAppointmentHistoryListScreen(viewModel = farmerHistoryViewModel)
                    }
                    composable(route = Routes.FarmerAppointmentDetail.route + "/{appointmentId}") {
                        val appointmentId = it.arguments?.getString("appointmentId")?.toLong() ?: 0
                        FarmerAppointmentDetailScreen(viewModel = farmerAppointmentDetailViewModel, appointmentId = appointmentId)
                    }
                    composable(route = Routes.CancelAppointmentConfirmation.route) {
                        CancelAppointmentSuccessScreen(onBackClick = {
                            navController.navigate(Routes.FarmerAppointmentList.route)
                        })
                    }
                    composable(route = Routes.FarmerReviewAppointment.route + "/{appointmentId}") {
                        val appointmentId = it.arguments?.getString("appointmentId")?.toLong() ?: 0
                        FarmerReviewAppointmentScreen(viewModel = farmerReviewAdvisorViewModel, appointmentId = appointmentId)
                    }
                    composable(route = Routes.SignUp.route) {
                        CreateAccountScreen(viewModel = createAccountViewModel)
                    }
                    composable(route = Routes.CreateAccountAdvisor.route) {
                        CreateAccountAdvisorScreen(viewModel = createAccountAdvisorViewModel)
                    }
                    composable(route = Routes.CreateAccountFarmer.route) {
                        CreateAccountFarmerScreen(viewModel = createAccountFarmerViewModel)
                    }
                    composable(route = Routes.CreateProfileAdvisor.route) {
                        CreateProfileAdvisorScreen(viewModel = createProfileAdvisorViewModel)
                    }
                    composable(route = Routes.CreateProfileFarmer.route) {
                        CreateProfileFarmerScreen(viewModel = createProfileFarmerViewModel)
                    }
                    composable(route = Routes.ConfirmDeletionAppointmentAdvisor.route) {
                        CancelAppointmentAdvisorSuccessScreen(viewModel = advisorAppointmentDetailViewModel)
                    }
                    composable(route = Routes.ConfirmCreationAccountAdvisor.route) {
                        ConfirmCreationAccountAdvisorScreen(viewModel = confirmCreationAccountAdvisorViewModel)
                    }
                    composable(route = Routes.ConfirmCreationAccountFarmer.route) {
                        ConfirmCreationAccountFarmerScreen(viewModel = confirmCreationAccountFarmerViewModel)
                    }
                    composable(route = Routes.NotificationList.route) {
                        NotificationListScreen(viewModel = notificationListViewModel)
                    }
                    composable(route = Routes.ExplorePosts.route) {
                        ExplorePostsScreen(viewModel = explorePostsViewModel)
                    }
                    composable(route = Routes.FarmerProfile.route) {
                        FarmerProfileScreen(viewModel = farmerProfileViewModel)
                    }
                    composable(route = Routes.AdvisorProfile.route) {
                        AdvisorProfileScreen(viewModel = advisorProfileViewModel)
                    }
                    composable(route = Routes.AdvisorPosts.route) {
                        AdvisorPostsScreen(viewModel = advisorPostsViewModel)
                    }
                    composable(route = Routes.AdvisorPostDetail.route + "/{postId}") {
                        val postId = it.arguments?.getString("postId")?.toLong() ?: 0
                        AdvisorPostDetailScreen(viewModel = advisorPostDetailViewModel, postId = postId)
                    }
                    composable(route = Routes.NewPost.route) {
                        NewPostScreen(viewModel = newPostViewModel)
                    }
                }
            }
        }
    }
}