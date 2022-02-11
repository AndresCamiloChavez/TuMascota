package com.devandreschavez.tumascota.viewmodel.reporter

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import com.devandreschavez.tumascota.core.Resource
import com.devandreschavez.tumascota.data.models.Pet
import com.devandreschavez.tumascota.repository.reporter.ReportRepository
import kotlinx.coroutines.Dispatchers

class ReporterViewModel(private val repo: ReportRepository): ViewModel() {

    fun uploadReporter(img: Uri?, pet: Pet) = liveData(Dispatchers.IO) {
        emit(Resource.Loading())
        try {
            emit(Resource.Success(repo.uploadReport(img, pet)))
        }catch (e: Exception){
            emit(Resource.Failure(e))
        }
    }
}
class FactoryReporterViewModel(private val repo: ReportRepository): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(ReportRepository::class.java).newInstance(repo)
    }
}