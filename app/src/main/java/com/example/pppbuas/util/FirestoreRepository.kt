package com.example.pppbuas.util

import com.google.firebase.firestore.CollectionReference
import kotlinx.coroutines.tasks.await

object FirestoreRepository {

    suspend fun <T : Any> addDocument(collectionReference: CollectionReference, data: T): Boolean {
        return try {
            collectionReference.add(data).await()
            true
        } catch (e: Exception) {
            false
        }
    }

    // Add more CRUD operations as needed
}
