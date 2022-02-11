package com.devandreschavez.tumascota.repository.petsuser

import com.devandreschavez.tumascota.core.Resource
import com.devandreschavez.tumascota.data.models.Pet
import com.devandreschavez.tumascota.data.remote.petsuser.PetsUserDataSource

class PetsUserRepositoryImpl(private val dataPetsUserSource: PetsUserDataSource): PetsUserRepository {
    override suspend fun getPetsByUser(userId: String): Resource<List<Pet>> = dataPetsUserSource.getPetsByUser(userId)
    override suspend fun findPetReport(userId: String, petId: String,  img: String) {
        dataPetsUserSource.reportFindPet(userId, petId,  img)
    }
}