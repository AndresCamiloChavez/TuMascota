package com.devandreschavez.tumascota.repository.reporter

import android.net.Uri
import com.devandreschavez.tumascota.data.models.Pet

interface ReportRepository {
    suspend fun uploadReport(img: Uri?, pet: Pet)
}