package com.example.agrotech.di

import com.example.agrotech.common.Constants
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
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkingModule {
    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideAdvisorService(retrofit: Retrofit): AdvisorService {
        return retrofit.create(AdvisorService::class.java)
    }

    @Provides
    @Singleton
    fun provideAnimalService(retrofit: Retrofit): AnimalService {
        return retrofit.create(AnimalService::class.java)
    }

    @Provides
    @Singleton
    fun provideAppointmentService(retrofit: Retrofit): AppointmentService {
        return retrofit.create(AppointmentService::class.java)
    }

    @Provides
    @Singleton
    fun provideAvailableDateService(retrofit: Retrofit): AvailableDateService {
        return retrofit.create(AvailableDateService::class.java)
    }

    @Provides
    @Singleton
    fun provideReviewService(retrofit: Retrofit): ReviewService {
        return retrofit.create(ReviewService::class.java)
    }

    @Provides
    @Singleton
    fun provideAuthenticationService(retrofit: Retrofit): AuthenticationService {
        return retrofit.create(AuthenticationService::class.java)
    }

    @Provides
    @Singleton
    fun provideEnclosureService(retrofit: Retrofit): EnclosureService {
        return retrofit.create(EnclosureService::class.java)
    }

    @Provides
    @Singleton
    fun provideFarmerService(retrofit: Retrofit): FarmerService {
        return retrofit.create(FarmerService::class.java)
    }

    @Provides
    @Singleton
    fun provideNotificationService(retrofit: Retrofit): NotificationService {
        return retrofit.create(NotificationService::class.java)
    }

    @Provides
    @Singleton
    fun providePostService(retrofit: Retrofit): PostService {
        return retrofit.create(PostService::class.java)
    }

    @Provides
    @Singleton
    fun provideProfileService(retrofit: Retrofit): ProfileService {
        return retrofit.create(ProfileService::class.java)
    }
}
