package com.example.pppbuas.util

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FirestoreManager<T : Any> {
    private val firestore = FirebaseFirestore.getInstance()

    suspend fun addData(collection: String, data: T) {
        firestore.collection(collection).add(data).await()
    }

    // Implement similar functions for update, delete, and getDataList
}