package com.example.agrotech.data.remote.appointment

import com.example.agrotech.domain.appointment.CreateReview
import com.example.agrotech.domain.appointment.UpdateReview
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ReviewService {
    @GET("reviews")
    suspend fun getReviews(@Header("Authorization") token: String): Response<List<ReviewDto>>

    
    @GET("reviews")
    suspend fun getReviewsByAdvisor(
        @Query("advisorId") advisorId: Long,
        @Header("Authorization") token: String
    ): Response<List<ReviewDto>>

    @POST("reviews")
    suspend fun createReview(
        @Header("Authorization") token: String, 
        @Body review: CreateReview
    ): Response<ReviewDto>

    @PUT("reviews/{reviewId}")
    suspend fun updateReview(
        @Path("reviewId") reviewId: Long,
        @Header("Authorization") token: String, 
        @Body review: UpdateReview
    ): Response<ReviewDto>

    @GET("reviews")
    suspend fun getReviewByAdvisorIdAndFarmerId(
        @Query("advisorId") advisorId: Long,
        @Query("farmerId") farmerId: Long,
        @Header("Authorization") token: String
    ): Response<List<ReviewDto>>
}
