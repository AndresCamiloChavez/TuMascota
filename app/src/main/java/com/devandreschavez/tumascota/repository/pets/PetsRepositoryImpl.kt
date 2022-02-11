package com.devandreschavez.tumascota.repository.pets

import android.net.Uri
import com.devandreschavez.tumascota.core.Resource
import com.devandreschavez.tumascota.data.models.PetUser
import com.devandreschavez.tumascota.data.remote.pets.PetsDataSource

class PetsRepositoryImpl(private val dataPetsSource: PetsDataSource) : PetsRespository {

    //futuro yo debe implementar room :) thanks
    override suspend fun getPets(): Resource<List<PetUser>> = dataPetsSource.getPetsHome()
    override suspend fun generateDynamicLink(
        namePet: String,
        descriptionPet: String,
        imgPet: String
    ): Resource<Uri?> = dataPetsSource.generateDynamicLink(namePet, descriptionPet, imgPet)
}