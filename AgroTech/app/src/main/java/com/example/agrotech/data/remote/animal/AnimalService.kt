package com.example.agrotech.data.remote.animal

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface AnimalService {
    @GET("animals")
    suspend fun getAnimalsByEnclosureId(
        @Header("Authorization") token: String,
        @Query("enclosureId") enclosureId: Long
    ): Response<List<AnimalDto>>

    @GET("animals/{id}")
    suspend fun getAnimalById(
        @Header("Authorization") token: String,
        @Path("id") id: Long
    ): Response<AnimalDto>

    @POST("animals")
    suspend fun createAnimal(
        @Header("Authorization") token: String,
        @Body animal: AnimalDto
    ): Response<AnimalDto>

    @PUT("animals/{id}")
    suspend fun updateAnimal(
        @Header("Authorization") token: String,
        @Path("id") id: Long,
        @Body animal: AnimalDto
    ): Response<AnimalDto>

    @DELETE("animals/{id}")
    suspend fun deleteAnimal(
        @Header("Authorization") token: String,
        @Path("id") id: Long
    ): Response<Unit>
}
