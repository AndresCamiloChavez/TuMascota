package com.devandreschavez.tumascota.repository.pets

import android.net.Uri
import com.devandreschavez.tumascota.core.Resource
import com.devandreschavez.tumascota.data.models.PetUser

interface PetsRespository {
    suspend fun getPets(): Resource<List<PetUser>>
    suspend fun generateDynamicLink(
        namePet: String,
        descriptionPet: String,
        imgPet: String): Resource<Uri?>
}