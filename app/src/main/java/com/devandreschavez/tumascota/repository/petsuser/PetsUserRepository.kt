package com.devandreschavez.tumascota.repository.petsuser

import com.devandreschavez.tumascota.core.Resource
import com.devandreschavez.tumascota.data.models.Pet

interface PetsUserRepository {
    suspend fun getPetsByUser(userId: String): Resource<List<Pet>>
    suspend fun findPetReport(userId: String, petId: String,  img: String): Unit
}