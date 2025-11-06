package com.example.agrotech.data.remote.enclosure

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface EnclosureService {
    @GET("enclosures")
    suspend fun getEnclosuresByFarmerId(
        @Header("Authorization") token: String,
        @Query("farmerId") farmerId: Long
    ): Response<List<EnclosureDto>>

    @GET("enclosures/{id}")
    suspend fun getEnclosureById(
        @Header("Authorization") token: String,
        @Path("id") id: Long
    ): Response<EnclosureDto>

    @POST("enclosures")
    suspend fun createEnclosure(
        @Header("Authorization") token: String, @Body enclosure: EnclosureDto
    ): Response<EnclosureDto>

    @PUT("enclosures/{id}")
    suspend fun updateEnclosure(
        @Header("Authorization") token: String,
        @Path("id") id: Long,
        @Body enclosure: EnclosureDto
    ): Response<EnclosureDto>

    @DELETE("enclosures/{id}")
    suspend fun deleteEnclosure(
        @Header("Authorization") token: String, @Path("id") id: Long
    ): Response<Unit>
}