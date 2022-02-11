package com.devandreschavez.tumascota.repository.reporter

import android.net.Uri
import com.devandreschavez.tumascota.data.models.Pet
import com.devandreschavez.tumascota.data.remote.reporter.ReporterDataSource

class ReporRepositoryImpl(private val reporterDataSource: ReporterDataSource): ReportRepository {
    override suspend fun uploadReport(img: Uri?, pet: Pet) {
        reporterDataSource.uploadReporter(img, pet)
    }
}