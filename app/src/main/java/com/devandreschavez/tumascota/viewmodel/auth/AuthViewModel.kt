package com.devandreschavez.tumascota.viewmodel.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.devandreschavez.tumascota.core.Resource
import com.devandreschavez.tumascota.data.models.User
import com.devandreschavez.tumascota.repository.auth.AuthRepository
import kotlinx.coroutines.Dispatchers

class AuthViewModel(private val authRepository: AuthRepository) : ViewModel() {

    fun signUpUser(user: User) = liveData(viewModelScope.coroutineContext + Dispatchers.Main) {
        emit(Resource.Loading())
        try {
            emit(Resource.Success(authRepository.signUp(user)))
        } catch (e: Exception) {
            emit(Resource.Failure(e))
        }
    }

    fun signInUser(email: String, password: String) =
        liveData(viewModelScope.coroutineContext + Dispatchers.Main) {
            emit(Resource.Loading())
            try {
                emit(Resource.Success(authRepository.signIn(email, password)))
            } catch (e: Exception){
                emit(Resource.Failure(e))
            }
        }
}

class FactoryAuthModel(private val authRepository: AuthRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(AuthRepository::class.java).newInstance(authRepository)
    }

}