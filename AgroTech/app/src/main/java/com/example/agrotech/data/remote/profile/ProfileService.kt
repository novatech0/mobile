package com.example.agrotech.data.remote.profile

import com.example.agrotech.domain.profile.CreateProfile
import com.example.agrotech.domain.profile.UpdateProfile
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface ProfileService {
    @Multipart
    @POST("profiles")
    suspend fun createProfile(
        @Header("Authorization") token: String,
        @Part("userId") userId: RequestBody,
        @Part("firstName") firstName: RequestBody,
        @Part("lastName") lastName: RequestBody,
        @Part("city") city: RequestBody,
        @Part("country") country: RequestBody,
        @Part("birthDate") birthDate: RequestBody,
        @Part("photo") photo: MultipartBody.Part,
        @Part("occupation") occupation: RequestBody?,
        @Part("description") description: RequestBody?,
        @Part("experience") experience: RequestBody?
    ): Response<ProfileDto>

    @GET("profiles/{userId}/user")
    suspend fun getProfile(@Path("userId") userId: Long, @Header("Authorization") token: String): Response<ProfileDto>

    @GET("profiles/advisors")
    suspend fun getAdvisors(@Header("Authorization") token: String): Response<List<ProfileDto>>

    @Multipart
    @PUT("profiles/{id}")
    suspend fun updateProfile(
        @Header("Authorization") token: String,
        @Path("id") id: Long,
        @Part("firstName") firstName: RequestBody,
        @Part("lastName") lastName: RequestBody,
        @Part("city") city: RequestBody,
        @Part("country") country: RequestBody,
        @Part("birthDate") birthDate: RequestBody,
        @Part("photo") photo: MultipartBody.Part?,
        @Part("occupation") occupation: RequestBody?,
        @Part("description") description: RequestBody?,
        @Part("experience") experience: RequestBody?
    ): Response<ProfileDto>
}