package com.example.pppbuas.user

import android.util.Log
import com.example.pppbuas.auth.AuthenticationManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await

object UserManager {
    interface UserDataCallback {
        fun onSuccess(userData: AppUser?)
        fun onFailure(error: String?)
    }

    private val usersCollection = FirebaseFirestore.getInstance().collection("users")
    private val TAG = "UserManager"
    suspend fun saveUserData(user: AppUser) {
        user.userId?.let {
            try {
                usersCollection.document(it).set(user).await()
                Log.d(TAG, "User data saved successfully")
            } catch (exception: Exception) {
                handleFirestoreException("Error saving user data", exception)
            }
        }
    }
    suspend fun getUserData(userId: String): AppUser? {
        return try {
            val documentSnapshot = usersCollection.document(userId).get().await()
            documentSnapshot.toObject(AppUser::class.java)
        } catch (e: Exception) {
            null
        }
    }
    suspend fun updateUserData(user: AppUser) {
        user.userId?.let {
            try {
                usersCollection.document(it).set(user).await()
                Log.d(TAG, "User data updated successfully")
            } catch (exception: Exception) {
                handleFirestoreException("Error updating user data", exception)
            }
        }
    }
    suspend fun deleteUserData(userId: String) {
        try {
            usersCollection.document(userId).delete().await()
            Log.d(TAG, "User data deleted successfully")
        } catch (exception: Exception) {
            handleFirestoreException("Error deleting user data", exception)
        }
    }
    private fun handleFirestoreException(message: String, exception: Exception) {
        Log.w(TAG, message, exception)
    }
    fun getUserRole(callback: (String?) -> Unit) {
        val currentUser = FirebaseAuth.getInstance().currentUser

        if (currentUser != null) {
            val userId = currentUser.uid

            usersCollection.document(userId).get()
                .addOnSuccessListener { documentSnapshot ->
                    val user = documentSnapshot.toObject<AppUser>()
                    val userRole = user?.role
                    callback.invoke(userRole)
                }
                .addOnFailureListener {
                    callback.invoke(null)
                }
        } else {
            // User is not authenticated
            callback.invoke(null)
        }
    }
}
