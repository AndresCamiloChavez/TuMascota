package com.devandreschavez.tumascota.repository.user

import com.devandreschavez.tumascota.core.Resource
import com.devandreschavez.tumascota.data.models.User

interface UserRepository {
    suspend fun getUser(): Resource<User>
    suspend fun updateUser(phone: String, address: String, urb: String):Resource.Success<Unit>
}