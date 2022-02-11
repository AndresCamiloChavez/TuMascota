package com.devandreschavez.tumascota.view.ui.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.devandreschavez.tumascota.R
import com.devandreschavez.tumascota.core.Resource
import com.devandreschavez.tumascota.data.models.User
import com.devandreschavez.tumascota.data.remote.auth.AuthDataSource
import com.devandreschavez.tumascota.databinding.FragmentRegisterBinding
import com.devandreschavez.tumascota.repository.auth.AuthRepoImpl
import com.devandreschavez.tumascota.viewmodel.auth.AuthViewModel
import com.devandreschavez.tumascota.viewmodel.auth.FactoryAuthModel

class RegisterFragment : Fragment(R.layout.fragment_register) {
    private lateinit var binding: FragmentRegisterBinding
    private val viewmodel by viewModels<AuthViewModel> {
        FactoryAuthModel(
            AuthRepoImpl(
                AuthDataSource()
            )
        )
    }
    private val TAG = "REGISTER"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentRegisterBinding.bind(view)
        binding.btnGoLogin.setOnClickListener {
            goToLogin()
        }
        binding.btnRegister.setOnClickListener {
            createUser()
        }
    }
    private fun goToLogin() {
        findNavController().navigate(R.id.loginFragment)
    }
    private fun createUser() {
        binding.apply {
            when {
                etEmailRegister.text.isNullOrEmpty() -> {
                    etEmailRegister.error = "Ingrese un correo electrónico"
                }
                etAddress.text.isNullOrEmpty() -> {
                    etAddress.error = "Ingrese una dirección"
                }
                etFullName.text.isNullOrEmpty() -> {
                    etFullName.error = "Ingrese un nombre y apellido"
                }
                etPhone.text.toString().length < 7 -> {
                    etPhone.error = "Ingrese un número de celular"
                }
                etPasswordRegister.text.toString().length < 7 -> {
                    etPasswordRegister.error = "Ingrese mayor a 6 caracteres"
                }
                etUrb.text.isNullOrEmpty() -> {
                    etPasswordRegister.error = "Ingrese una urbanización o sector"
                }
                else -> {
                    val user = User(
                        etFullName.text.toString(),
                        "0",
                        etPhone.text.toString(),
                        etAddress.text.toString(),
                        etEmailRegister.text.toString(),
                        etPasswordRegister.text.toString(),
                        etUrb.text.toString()
                    )
                    viewmodel.signUpUser(user).observe(viewLifecycleOwner, Observer {
                        when (it) {
                            is Resource.Loading -> {
                                binding.progressRegister.visibility = View.VISIBLE
                                Log.d(TAG, "Cargando registro ${it}")
                            }
                            is Resource.Success -> {
                                findNavController().navigate(R.id.action_registerFragment_to_petsFragment)
                                Log.d(TAG, "Cargando registro ${it}")

                            }
                            is Resource.Failure -> {
                                binding.progressRegister.visibility = View.GONE
                                Toast.makeText(
                                    requireContext(),
                                    "Ocurrió un error, intente de nuevo",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    })
                }
            }
        }
    }

}