package com.devandreschavez.tumascota.data.remote.pets

import android.net.Uri
import com.devandreschavez.tumascota.application.AppConstants
import com.devandreschavez.tumascota.core.Resource
import com.devandreschavez.tumascota.data.models.Pet
import com.devandreschavez.tumascota.data.models.PetUser
import com.devandreschavez.tumascota.data.models.User
import com.google.firebase.dynamiclinks.DynamicLink
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class PetsDataSource {
    suspend fun getPetsHome(): Resource<List<PetUser>> {
        val petUsersList = mutableListOf<PetUser>()
        withContext(Dispatchers.IO) {

            val querySnapshot =
                FirebaseFirestore.getInstance().collection(AppConstants.collectionPets)
                    .orderBy("date", Query.Direction.ASCENDING).get()
                    .await()
            for (pet in querySnapshot.documents) {
                pet.toObject(Pet::class.java).let { resultPet ->
                    resultPet?.let { petResult ->
                        resultPet.id = pet.id
                        val user =
                            FirebaseFirestore.getInstance().collection(AppConstants.collectionUser)
                                .document(pet.getString("userId")!!).get().await()
                                .toObject(User::class.java)
                        user?.let { resultUser ->
                            petUsersList.add(PetUser(petResult, resultUser))
                        }
                    }
                }
            }
        }
        return Resource.Success(petUsersList)
    }

    suspend fun generateDynamicLink(
        namePet: String,
        descriptionPet: String,
        imgPet: String
    ): Resource<
            Uri?> {
        val dynamicLink = FirebaseDynamicLinks.getInstance().createDynamicLink()
            //mejorar esta parte porque debemos identificar de alguna manera el intent para redirigir
            .setLink(Uri.parse("https://tumascota.page.link/29hQ?pet=${namePet}"))
            .setDomainUriPrefix("https://tumascota.page.link").setAndroidParameters(
                DynamicLink.AndroidParameters.Builder("com.devandreschavez.tumascota")
                    .setMinimumVersion(1).build()
            ).setIosParameters(
                DynamicLink.IosParameters.Builder("com.devandreschavez.tumascota")
                    .setMinimumVersion("1.0.1").build()
            ).setSocialMetaTagParameters(
                DynamicLink.SocialMetaTagParameters.Builder()
                    .setTitle("Ayúdame a buscar a $namePet")
                    .setDescription(descriptionPet)
                    .setImageUrl(Uri.parse(imgPet))
                    .build()
            ).buildShortDynamicLink().await()
        return Resource.Success(dynamicLink.shortLink)
    }

}