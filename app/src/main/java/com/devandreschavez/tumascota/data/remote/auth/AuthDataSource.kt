package com.devandreschavez.tumascota.data.remote.auth

import com.devandreschavez.tumascota.application.AppConstants
import com.devandreschavez.tumascota.data.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class AuthDataSource {
    suspend fun signUp(user: User): FirebaseUser? {
        return withContext(Dispatchers.IO){
            val authResult = FirebaseAuth.getInstance()
                .createUserWithEmailAndPassword(user.email.toString(), user.password.toString()).await()
            authResult.user?.uid?.let {id ->
                FirebaseFirestore.getInstance().collection(AppConstants.collectionUser).document(id).set(user)
            }
            authResult.user
        }
    }
    suspend fun signIn(email: String, password: String): FirebaseUser?{
        return  withContext(Dispatchers.IO){
            val authResult = FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).await()
            authResult.user
        }
    }

}