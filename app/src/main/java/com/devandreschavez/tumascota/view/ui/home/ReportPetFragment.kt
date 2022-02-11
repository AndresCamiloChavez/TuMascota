package com.devandreschavez.tumascota.view.ui.home

import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.devandreschavez.tumascota.R
import com.devandreschavez.tumascota.core.Resource
import com.devandreschavez.tumascota.data.models.Pet
import com.devandreschavez.tumascota.data.remote.reporter.ReporterDataSource
import com.devandreschavez.tumascota.databinding.FragmentReportPetBinding
import com.devandreschavez.tumascota.repository.reporter.ReporRepositoryImpl
import com.devandreschavez.tumascota.viewmodel.reporter.FactoryReporterViewModel
import com.devandreschavez.tumascota.viewmodel.reporter.ReporterViewModel
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import java.time.LocalDate
import java.util.*


class ReportPetFragment : Fragment(R.layout.fragment_report_pet) {
    private lateinit var binding: FragmentReportPetBinding
    private lateinit var pet: Pet
    private var imgRefUri: Uri? = null
    private val user = FirebaseAuth.getInstance().currentUser
    private val viewmodel: ReporterViewModel by viewModels {
        FactoryReporterViewModel(
            ReporRepositoryImpl(
                ReporterDataSource()
            )
        )
    }

    private var launchResponse =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == AppCompatActivity.RESULT_OK) {
                binding.imgReportPet.setImageURI(result.data?.data)
                imgRefUri = result.data?.data
            } else {
                Toast.makeText(requireContext(), "No selecciono ninguna imagen", Toast.LENGTH_SHORT)
                    .show()
            }
        }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentReportPetBinding.bind(view)
        val constraints =
            CalendarConstraints.Builder().setEnd(MaterialDatePicker.thisMonthInUtcMilliseconds())
                .build()

        binding.etDateReportPet.setText("${LocalDate.now().dayOfMonth}/${LocalDate.now().monthValue}/${LocalDate.now().year}")

        setData()

        binding.etDateReportPet.setOnClickListener {
            val datePicker =
                MaterialDatePicker.Builder.datePicker()
                    .setCalendarConstraints(constraints)
                    .setTitleText("Fecha de desaparición")
                    .build()

            datePicker.show(childFragmentManager, "Date")
            datePicker.addOnPositiveButtonClickListener {
                val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
                calendar.time = Date(it)
                binding.etDateReportPet.setText(
                    "${calendar.get(Calendar.DAY_OF_MONTH)}/${calendar.get(Calendar.MONTH) + 1}/${
                        calendar.get(Calendar.YEAR)
                    }"
                )
            }
        }

        binding.btnUpPhoto.setOnClickListener {
            requestPermission()
        }
        binding.btnReporterPet.setOnClickListener {
            uploadPicture()
        }

    }

    private fun setPhotoFromGalery() {
        val takePictureIntent = Intent(Intent.ACTION_GET_CONTENT)
        takePictureIntent.type = "image/*"
        try {
            launchResponse.launch(takePictureIntent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(
                requireContext(),
                "No se encontro aplicación para abrir la galeria",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun setData() {
        val typePet = listOf("Perro", "Gato", "Otro")
        val adapterPet = ArrayAdapter(requireContext(), R.layout.list_item, typePet)
        (binding.menuType.editText as? AutoCompleteTextView)?.setAdapter(adapterPet)
        val typeSex = listOf("Macho", "Hembra")
        val adapterSex = ArrayAdapter(requireContext(), R.layout.list_item, typeSex)
        (binding.menuSex.editText as? AutoCompleteTextView)?.setAdapter(adapterSex)

    }

    private fun requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            when {
                ContextCompat.checkSelfPermission(
                    requireContext(),
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED -> {
                    setPhotoFromGalery()
                }
                else -> requestPermissionLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        } else {
            setPhotoFromGalery()
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        if (it) {
            setPhotoFromGalery()
        } else {
            Toast.makeText(requireContext(), "Necesita activar permisos", Toast.LENGTH_SHORT).show()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun uploadPicture() {
        val dialog = MaterialAlertDialogBuilder(requireContext()).setView(R.layout.loadingdialog)
            .setCancelable(false)
            .setTitle("!Ánimo!, volverá pronto")
            .create()
        //validaciones
        if (validations()) {
            val pet = Pet(
                namePet = binding.etNamePet.text.toString(),
                userId = user!!.uid,
                date = binding.etDateReportPet.text.toString(),
                typeAnimal = binding.etMenuType.text.toString(),
                pictureAnimal = "",
                sector = binding.etSector.text.toString(),
                sex = binding.etMenuSex.text.toString(),
                description = binding.etDescriptionReportPet.text.toString(),
                status = true,
                publicationDate = Calendar.getInstance().time.toString()
            )
            binding.btnReporterPet.isEnabled = false
            viewmodel.uploadReporter(imgRefUri, pet)
                .observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                    when (it) {
                        is Resource.Loading -> {
                            dialog.show()
                        }
                        is Resource.Success -> {
                            dialog.cancel()
                            findNavController().navigate(R.id.petsFragment)
                            binding.btnReporterPet.isEnabled = true
                        }
                        is Resource.Failure -> {
                            dialog.cancel()
                            binding.btnReporterPet.isEnabled = true

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

    private fun validations(): Boolean {
        return when {
            binding.etNamePet.text?.trim().isNullOrEmpty() -> {
                binding.etNamePet.error = "Ingrese un nombre"
                false
            }
            binding.etSector.text.isNullOrEmpty() -> {
                binding.etSector.error = "Ingrese un valor"
                false
            }
            binding.etMenuSex.text.isNullOrEmpty() -> {
                binding.etMenuSex.error = "Seleccione un valor"
                false
            }
            binding.etMenuType.text.isNullOrEmpty() -> {
                binding.etMenuSex.error = null
                binding.etMenuType.error = "Seleccione un valor"
                false
            }
            binding.etDescriptionReportPet.text.isNullOrEmpty() -> {

                binding.etDescriptionReportPet.error = "Agregue una descripción "
                false
            }
            else -> {
                true
            }
        }
    }
}