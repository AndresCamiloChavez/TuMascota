package com.devandreschavez.tumascota.repository.auth

import com.devandreschavez.tumascota.data.models.User
import com.devandreschavez.tumascota.data.remote.auth.AuthDataSource
import com.google.firebase.auth.FirebaseUser

class AuthRepoImpl(private val authDataSource: AuthDataSource): AuthRepository {
    override suspend fun signUp(user: User): FirebaseUser? = authDataSource.signUp(user)
    override suspend fun signIn(email: String, password: String): FirebaseUser? = authDataSource.signIn(email,password)
}