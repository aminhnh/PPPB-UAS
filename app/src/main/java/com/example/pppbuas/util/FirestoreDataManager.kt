package com.example.pppbuas.util

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.tasks.await

class FirestoreDataManager {

    private val firestore = FirebaseFirestore.getInstance()

    suspend fun getCityByName(cityName: String): List<String> {
        try {
            val querySnapshot: QuerySnapshot =
                firestore.collection("cities").whereEqualTo("name", cityName).get().await()

            val cities = mutableListOf<String>()

            for (document in querySnapshot.documents) {
                val city = document.getString("name")
                city?.let { cities.add(it) }
            }

            return cities
        } catch (e: Exception) {
            // Handle exception (e.g., log or throw)
            return emptyList()
        }
    }
}
