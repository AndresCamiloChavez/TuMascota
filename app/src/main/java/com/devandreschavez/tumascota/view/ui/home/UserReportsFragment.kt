package com.devandreschavez.tumascota.view.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.devandreschavez.samaca.view.adapter.PetsByUserAdapter
import com.devandreschavez.tumascota.R
import com.devandreschavez.tumascota.core.Resource
import com.devandreschavez.tumascota.data.remote.petsuser.PetsUserDataSource
import com.devandreschavez.tumascota.databinding.FragmentUserReportsBinding
import com.devandreschavez.tumascota.repository.petsuser.PetsUserRepositoryImpl
import com.devandreschavez.tumascota.viewmodel.petsuser.FactoryPetsUserViewModel
import com.devandreschavez.tumascota.viewmodel.petsuser.PetsUserViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth

class UserReportsFragment : Fragment(R.layout.fragment_user_reports),
    PetsByUserAdapter.onPetUserClickListener {
    private lateinit var binding: FragmentUserReportsBinding
    private val user = FirebaseAuth.getInstance().currentUser
    private val viewModel: PetsUserViewModel by viewModels {
        FactoryPetsUserViewModel(
            PetsUserRepositoryImpl(
                PetsUserDataSource()
            )
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentUserReportsBinding.bind(view)

        loadInfo()
        binding.btnBackPets.setOnClickListener {
            findNavController().navigate(R.id.action_userReportsFragment_to_petsFragment)
        }
    }

    private fun loadInfo() {
        user?.let {
            viewModel.getPetsByUser(it.uid).observe(viewLifecycleOwner, Observer { listPetsUser ->
                when (listPetsUser) {
                    is Resource.Loading -> {
                        binding.progressPetsUser.visibility = View.VISIBLE
                    }
                    is Resource.Success -> {
                        binding.progressPetsUser.visibility = View.GONE
                        binding.rvPetsUser.adapter = PetsByUserAdapter(listPetsUser.data, this)
                        if (listPetsUser.data.isEmpty()) {
                            binding.containerBackUserPets.visibility = View.VISIBLE
                            return@Observer
                        }
                    }
                    is Resource.Failure -> {
                        binding.progressPetsUser.visibility = View.GONE
                        Toast.makeText(requireContext(), "Ocurrió un error", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            })
        }
    }

    override fun onReportPetUser(userId: String, petId: String, img: String) {
        MaterialAlertDialogBuilder(requireContext()).setTitle("¿Estas seguro eliminar la publicación?")
            .setMessage("Espero que te hayamos ayudado muchas gracias por usar Samacá")
            .setNegativeButton("Cancelar") { dialog, which ->
                dialog.cancel()
            }
            .setPositiveButton("Aceptar") { dialog, which ->
                viewModel.findPetReport(userId, petId, img).observe(viewLifecycleOwner, Observer {
                    when (it) {
                        is Resource.Loading -> {
                            Toast.makeText(requireContext(), "Cargando...", Toast.LENGTH_SHORT)
                                .show()
                        }
                        is Resource.Success -> {
                            loadInfo()
                            Toast.makeText(
                                requireContext(),
                                "Se elimino la publicación exitosamente",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }
                        is Resource.Failure -> {
//                            Log.d("report", "${it.e}")
                            Toast.makeText(
                                requireContext(),
                                "Ocurrió un error al eliminar",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                })
            }.show()

    }
}