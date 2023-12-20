package com.example.pppbuas.auth

interface SignUpResultCallback {
    fun onSignUpSuccess()
    fun onSignUpFailure(errorMessage: String?)
}