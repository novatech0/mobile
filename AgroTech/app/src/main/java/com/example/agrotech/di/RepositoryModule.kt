package com.example.agrotech.di

import com.example.agrotech.data.local.UserDao
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
import com.example.agrotech.data.repository.authentication.RegistrationDataRepository
import com.example.agrotech.data.repository.enclosure.EnclosureRepository
import com.example.agrotech.data.repository.farmer.FarmerRepository
import com.example.agrotech.data.repository.notification.NotificationRepository
import com.example.agrotech.data.repository.post.PostRepository
import com.example.agrotech.data.repository.profile.ProfileRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides @Singleton
    fun provideProfileRepository(service: ProfileService) = ProfileRepository(service)

    @Provides @Singleton
    fun provideAdvisorRepository(service: AdvisorService) = AdvisorRepository(service)

    @Provides @Singleton
    fun provideFarmerRepository(service: FarmerService) = FarmerRepository(service)

    @Provides @Singleton
    fun provideAppointmentRepository(service: AppointmentService) = AppointmentRepository(service)

    @Provides @Singleton
    fun provideAvailableDateRepository(service: AvailableDateService) = AvailableDateRepository(service)

    @Provides @Singleton
    fun provideReviewRepository(service: ReviewService) = ReviewRepository(service)

    @Provides @Singleton
    fun provideNotificationRepository(service: NotificationService) = NotificationRepository(service)

    @Provides @Singleton
    fun providePostRepository(service: PostService) = PostRepository(service)

    @Provides @Singleton
    fun provideAuthenticationRepository(
        service: AuthenticationService,
        userDao: UserDao
    ) = AuthenticationRepository(service, userDao)

    @Provides @Singleton
    fun provideEnclosureRepository(service: EnclosureService) = EnclosureRepository(service)

    @Provides @Singleton
    fun provideAnimalRepository(service: AnimalService) = AnimalRepository(service)

    @Provides
    @Singleton
    fun provideRegistrationDataRepository(): RegistrationDataRepository = RegistrationDataRepository()
}
