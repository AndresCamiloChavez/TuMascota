package com.devandreschavez.tumascota.repository.auth

import com.devandreschavez.tumascota.data.models.User
import com.google.firebase.auth.FirebaseUser

interface AuthRepository {
    suspend fun signUp(user: User): FirebaseUser?
    suspend fun signIn(email: String, password: String): FirebaseUser?
}