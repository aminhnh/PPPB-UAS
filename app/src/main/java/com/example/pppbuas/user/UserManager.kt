package com.example.pppbuas.user

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class UserManager {
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
    fun getUserData(userId: String, callback: UserDataCallback) {
        try {
            usersCollection.document(userId).get()
                .addOnSuccessListener { document ->
                    val userData = document.toObject(AppUser::class.java)
                    callback.onSuccess(userData)
                }
                .addOnFailureListener { exception ->
                    callback.onFailure(exception.message)
                }
        } catch (e: Exception) {
            callback.onFailure("Error getting user data")
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
        // You can add additional error handling logic here if needed
    }

}
