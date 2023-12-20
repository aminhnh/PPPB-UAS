package com.example.pppbuas.auth

interface SignInResultCallback {
    fun onSignInSuccess()
    fun onSignInFailure(errorMessage: String?)
}