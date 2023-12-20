package com.example.pppbuas.auth

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

class AuthenticationManager(
    private val signUpResultCallback: SignUpResultCallback,
    private val signInResultCallback: SignInResultCallback
) {
    private val TAG = "AuthenticationManager"
    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    companion object {
        @Volatile
        private var instance: AuthenticationManager? = null
        fun getInstance(
            signUpResultCallback: SignUpResultCallback,
            signInResultCallback: SignInResultCallback
        ): AuthenticationManager {
            return instance ?: synchronized(this) {
                instance ?: AuthenticationManager(signUpResultCallback, signInResultCallback)
                    .also { instance = it }
            }
        }
    }

    fun signUpUser(email: String, password: String, fullName: String, phoneNumber: String, nim: String) {
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = mAuth.currentUser
                    val userId = user?.uid

                    userId?.let {
                        val userMap = hashMapOf(
                            "email" to email,
                            "role" to "user",
                            "fullName" to fullName,
                            "phoneNumber" to phoneNumber,
                            "nim" to nim,
                        )

                        val usersCollection = FirebaseFirestore.getInstance().collection("users")
                        usersCollection.document(it).set(userMap)
                            .addOnSuccessListener {
                                Log.w(TAG, "Additional user data saved successfully")
                                signUpResultCallback.onSignUpSuccess()
                            }
                            .addOnFailureListener { exception ->
                                Log.w(TAG, "Error saving additional user data", exception)
                                signUpResultCallback.onSignUpFailure(exception.message)
                            }
                    }
                } else {
                    val exception = task.exception
                    Log.w(TAG, "Sign up failed", exception)

                    val errorMessage = exception?.message
                    signUpResultCallback.onSignUpFailure(errorMessage)
                }
            }
    }
    fun signInUser(email: String, password: String) {
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {task ->
                if (task.isSuccessful) {
                    Log.w(TAG, "Sign in successful")
                    signInResultCallback.onSignInSuccess()
                } else {
                    val exception = task.exception
                    Log.w(TAG, "Sign in failed", exception)

                    val errorMessage = exception?.message
                    signInResultCallback.onSignInFailure(errorMessage)                }
            }
    }
    fun signOut() {
        try {
            mAuth.signOut()
            Log.d(TAG, "User signed out successfully")
        } catch (e: Exception) {
            Log.e(TAG, "Error signing out: ${e.message}", e)
        }
    }
}