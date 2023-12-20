package com.example.pppbuas.util

import com.google.firebase.firestore.FirebaseFirestore

object FirestoreManager {
    val db: FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }
}