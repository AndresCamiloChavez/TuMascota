package com.devandreschavez.tumascota.repository.user

import com.devandreschavez.tumascota.core.Resource
import com.devandreschavez.tumascota.data.models.User
import com.devandreschavez.tumascota.data.remote.user.UserDataSource

class UserRepositoryImpl(private val dataUserSource: UserDataSource) :UserRepository {
    override suspend fun getUser(): Resource<User> = dataUserSource.getUser()
    override suspend fun updateUser(phone: String, address: String, urb: String): Resource.Success<Unit> = dataUserSource.updateUser(phone,address,urb)
}