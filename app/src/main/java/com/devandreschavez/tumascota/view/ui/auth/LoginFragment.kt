package com.devandreschavez.tumascota.view.ui.auth

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.core.util.PatternsCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.devandreschavez.tumascota.R
import com.devandreschavez.tumascota.core.Resource
import com.devandreschavez.tumascota.data.remote.auth.AuthDataSource
import com.devandreschavez.tumascota.databinding.FragmentLoginBinding
import com.devandreschavez.tumascota.repository.auth.AuthRepoImpl
import com.devandreschavez.tumascota.viewmodel.auth.AuthViewModel
import com.devandreschavez.tumascota.viewmodel.auth.FactoryAuthModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth

class LoginFragment : Fragment(R.layout.fragment_login) {
    private lateinit var binding: FragmentLoginBinding
    private val firebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val viewModel: AuthViewModel by viewModels {
        FactoryAuthModel(
            AuthRepoImpl(
                AuthDataSource()
            )
        )
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLoginBinding.bind(view)
        Glide.with(requireContext())
            .load("https://firebasestorage.googleapis.com/v0/b/samacacuida.appspot.com/o/assets%2Fpexels-lumn-406014.jpg?alt=media&token=45e14dc8-c259-4d89-bdba-0215ba47ee54")
            .into(binding.imgLogin).onLoadFailed(getDrawable(requireContext(),R.drawable.noimage))
        if (firebaseAuth.currentUser != null) {
            goHomeActivity()
        }
        binding.btnGoRegister.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
        binding.btnLogin.setOnClickListener {
            if (validateEmail()) {
                isUserLogin()
            }
        }
    }
    private fun isUserLogin() {
        binding.btnLogin.isEnabled = false
        binding.btnGoRegister.isEnabled = false
        val dialog = MaterialAlertDialogBuilder(requireContext()).setView(R.layout.loadingdialog)
            .setCancelable(false)
            .setTitle("Esto tomará unos segundos")
            .create()
        viewModel.signInUser(binding.etEmail.text.toString(), binding.etPassword.text.toString())
            .observe(viewLifecycleOwner, Observer { result ->

                when (result) {
                    is Resource.Loading -> {
                        dialog.show()
                    }
                    is Resource.Success -> {
                        dialog.cancel()
                        binding.progressLoginBar.visibility = View.GONE
                        goHomeActivity()
                    }
                    is Resource.Failure -> {
                        dialog.cancel()
                        binding.progressLoginBar.visibility = View.GONE
                        binding.btnLogin.isEnabled = true
                        binding.btnGoRegister.isEnabled = true
                        Toast.makeText(
                            requireContext(),
                            "Ocurrió un error, usuario no válido",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            })
    }
    private fun validateEmail(): Boolean {
        return when {
            binding.etEmail.text.isNullOrEmpty() -> {
                binding.etEmail.error = "Campo vacío"
                false
            }
            !PatternsCompat.EMAIL_ADDRESS.matcher(binding.etEmail.text).matches() -> {
                binding.etEmail.error = "Formato de correo no válido"
                false
            }
            else -> {
                binding.etEmail.error = null
                true
            }
        }
    }
    private fun goHomeActivity() {
        findNavController().navigate(R.id.action_loginFragment_to_petsFragment)
    }

}