package com.devandreschavez.tumascota.data.remote.reporter

import android.net.Uri
import com.devandreschavez.tumascota.application.AppConstants
import com.devandreschavez.tumascota.data.models.Pet
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class ReporterDataSource {
    suspend fun uploadReporter(img: Uri?, pet: Pet) {
        withContext(Dispatchers.IO) {
            if (img != null) {
                val petFirebase =
                    FirebaseFirestore.getInstance().collection(AppConstants.collectionPets)
                        .add(pet).await()
                val imageRef =
                    FirebaseStorage.getInstance().reference.child("reportpets/${pet.userId}/${petFirebase.id}")
                val downloadUrl =
                    imageRef.putFile(img).await().storage.downloadUrl.await().toString()
                FirebaseFirestore.getInstance().collection(AppConstants.collectionPets)
                    .document(petFirebase.id).update("pictureAnimal", downloadUrl)
            } else {
                FirebaseFirestore.getInstance().collection(AppConstants.collectionPets)
                    .add(pet.apply {
                        pictureAnimal =
                            "https://firebasestorage.googleapis.com/v0/b/samacacuida.appspot.com/o/assets%2FnoImage.jpg?alt=media&token=776b9d1a-4ccf-419a-8326-84a68525fa1a"
                    }).await()
            }
        }
    }
}