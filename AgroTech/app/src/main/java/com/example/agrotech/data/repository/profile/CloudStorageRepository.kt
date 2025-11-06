package com.example.agrotech.data.repository.profile

import android.net.Uri
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.tasks.await

class CloudStorageRepository {

    private val storage = Firebase.storage
    private val storageRef = storage.reference

    private fun getStorageReference() = storageRef.child("photos")

    suspend fun uploadFile(filename: String, path: Uri): String {
        val file = getStorageReference().child(filename)
        val uploadTask = file.putFile(path).await()
        return file.downloadUrl.await().toString()
    }

    suspend fun getUserImages(): List<String> {
        val imagesUrl = mutableListOf<String>()
        val listResult = getStorageReference().listAll().await()
        for (item in listResult.items) {
            val url = item.downloadUrl.await().toString()
            imagesUrl.add(url)
        }
        return imagesUrl
    }
}