package com.devandreschavez.tumascota.viewmodel.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import com.devandreschavez.tumascota.core.Resource
import com.devandreschavez.tumascota.repository.user.UserRepository
import java.lang.Exception

class UserViewModel(private val userRepository: UserRepository): ViewModel() {

    val fetchUserL = liveData {
        emit(Resource.Loading())
        try {
            emit(userRepository.getUser())
        }catch (e: Exception){
            emit(Resource.Failure(e))
        }
    }
    fun fetchUser() = liveData {
        emit(Resource.Loading())
        try {
            emit(userRepository.getUser())
        }catch (e: Exception){
            emit(Resource.Failure(e))
        }
    }
    fun updateUser(phone: String, address: String, urb: String) = liveData {
        emit(Resource.Loading())
        try {
            emit(userRepository.updateUser(phone, address, urb))
        }catch (e: Exception){
            emit(Resource.Failure(e))
        }
    }
}
class FactoryUserViewModel(private val userRepository: UserRepository): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(UserRepository::class.java).newInstance(userRepository)
    }
}