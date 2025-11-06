package com.example.agrotech.data.repository.animal

import com.example.agrotech.common.Resource
import com.example.agrotech.data.remote.animal.AnimalService
import com.example.agrotech.data.remote.animal.toAnimal
import com.example.agrotech.domain.animal.Animal
import com.example.agrotech.domain.animal.toAnimalDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AnimalRepository(private val animalService: AnimalService) {

    suspend fun getAnimalsByEnclosureId(token: String, enclosureId: Long): Resource<List<Animal>> = withContext(Dispatchers.IO) {
        if (token.isBlank()) {
            return@withContext Resource.Error(message = "Un token es requerido")
        }
        val bearerToken = "Bearer $token"
        val response = animalService.getAnimalsByEnclosureId(bearerToken, enclosureId)
        if (response.isSuccessful) {
            response.body()?.let { animalsDto ->
                val animals = animalsDto.map { it.toAnimal() }
                return@withContext Resource.Success(animals)
            }
            return@withContext Resource.Error(message = "Error al obtener animales")
        }
        return@withContext Resource.Error(response.message())
    }

    suspend fun getAnimalById(token: String, id: Long): Resource<Animal> = withContext(Dispatchers.IO) {
        if (token.isBlank()) {
            return@withContext Resource.Error(message = "Un token es requerido")
        }
        val bearerToken = "Bearer $token"
        val response = animalService.getAnimalById(bearerToken, id)
        if (response.isSuccessful) {
            response.body()?.let { animalDto ->
                val animal = animalDto.toAnimal()
                return@withContext Resource.Success(animal)
            }
            return@withContext Resource.Error(message = "Error al obtener el animal")
        }
        return@withContext Resource.Error(response.message())
    }

    suspend fun createAnimal(token: String, animal: Animal): Resource<Animal> = withContext(Dispatchers.IO) {
        if (token.isBlank()) {
            return@withContext Resource.Error(message = "Un token es requerido")
        }
        val bearerToken = "Bearer $token"
        val response = animalService.createAnimal(bearerToken, animal.toAnimalDto())
        if (response.isSuccessful) {
            response.body()?.let { animalDto ->
                val createdAnimal = animalDto.toAnimal()
                return@withContext Resource.Success(createdAnimal)
            }
            return@withContext Resource.Error(message = "No se pudo crear el animal")
        }
        return@withContext Resource.Error(response.message())
    }

    suspend fun updateAnimal(token: String, animal: Animal): Resource<Animal> = withContext(Dispatchers.IO) {
        if (token.isBlank()) {
            return@withContext Resource.Error(message = "Un token es requerido")
        }
        val bearerToken = "Bearer $token"
        val response = animalService.updateAnimal(bearerToken, animal.id, animal.toAnimalDto())
        if (response.isSuccessful) {
            response.body()?.let { animalDto ->
                val updatedAnimal = animalDto.toAnimal()
                return@withContext Resource.Success(updatedAnimal)
            }
            return@withContext Resource.Error(message = "No se pudo actualizar el animal")
        }
        return@withContext Resource.Error(response.message())
    }

    suspend fun deleteAnimal(token: String, animalId: Long): Resource<Unit> = withContext(Dispatchers.IO) {
        if (token.isBlank()) {
            return@withContext Resource.Error(message = "Un token es requerido")
        }
        val bearerToken = "Bearer $token"
        val response = animalService.deleteAnimal(bearerToken, animalId)
        if (response.isSuccessful) {
            return@withContext Resource.Success(Unit)
        }
        return@withContext Resource.Error(response.message())
    }
}
