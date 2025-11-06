package com.example.agrotech.data.repository.appointment

import com.example.agrotech.common.Resource
import com.example.agrotech.data.remote.appointment.ReviewService
import com.example.agrotech.data.remote.appointment.toReview
import com.example.agrotech.domain.appointment.CreateReview
import com.example.agrotech.domain.appointment.Review
import com.example.agrotech.domain.appointment.UpdateReview
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ReviewRepository(private val reviewService: ReviewService) {
    suspend fun getAdvisorReviewsList(advisorId: Long, token: String): Resource<List<Review>> = withContext(Dispatchers.IO) {
        if (token.isBlank()) {
            return@withContext Resource.Error(message = "Un token es requerido")
        }
        val bearerToken = "Bearer $token"
        val response = reviewService.getReviewsByAdvisor(advisorId, bearerToken)
        if (response.isSuccessful) {
            response.body()?.let { reviewDtos ->
                val reviews = reviewDtos.map { it.toReview() }
                return@withContext Resource.Success(data = reviews)
            }
            return@withContext Resource.Error(message = "Error al obtener lista de reseñas")
        }
        return@withContext Resource.Error(response.message())
    }

    suspend fun createReview(token: String, review: Review): Resource<Review> = withContext(Dispatchers.IO) {
        if (token.isBlank()) {
            return@withContext Resource.Error(message = "Un token es requerido")
        }
        val bearerToken = "Bearer $token"
        val response = reviewService.createReview(bearerToken,
            CreateReview(review.advisorId, review.farmerId, review.comment, review.rating)
        )
        if (response.isSuccessful) {
            response.body()?.let { reviewDto ->
                val createdReview = reviewDto.toReview()
                return@withContext Resource.Success(createdReview)
            }
            return@withContext Resource.Error(message = "No se pudo crear la reseña")
        }
        return@withContext Resource.Error(response.message())
    }

    suspend fun updateReview(token: String, reviewId: Long, review: Review): Resource<Review> = withContext(Dispatchers.IO) {
        if (token.isBlank()) {
            return@withContext Resource.Error(message = "Un token es requerido")
        }
        val bearerToken = "Bearer $token"
        val response = reviewService.updateReview(reviewId, bearerToken,
            UpdateReview(review.comment, review.rating)
        )
        if (response.isSuccessful) {
            response.body()?.let { reviewDto ->
                val updatedReview = reviewDto.toReview()
                return@withContext Resource.Success(updatedReview)
            }
            return@withContext Resource.Error(message = "No se pudo actualizar la reseña")
        }
        return@withContext Resource.Error(response.message())
    }

    suspend fun getReviewByAdvisorIdAndFarmerId(advisorId: Long, farmerId: Long, token: String): Resource<Review> = withContext(Dispatchers.IO) {
        if (token.isBlank()) {
            return@withContext Resource.Error(message = "Un token es requerido")
        }
        val bearerToken = "Bearer $token"
        val response = reviewService.getReviewByAdvisorIdAndFarmerId(advisorId, farmerId, bearerToken)
        if (response.isSuccessful) {
            response.body()?.let { reviewDtos ->
                val review = reviewDtos[0].toReview()
                return@withContext Resource.Success(data = review)
            }
            return@withContext Resource.Error(message = "Error al obtener la reseña")
        }

        return@withContext Resource.Error(response.message())
    }


}