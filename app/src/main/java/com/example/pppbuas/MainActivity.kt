package com.example.pppbuas

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.viewpager2.widget.ViewPager2
import com.example.pppbuas.auth.AuthenticationManager
import com.example.pppbuas.auth.SignInResultCallback
import com.example.pppbuas.auth.SignUpResultCallback
import com.example.pppbuas.auth.TabAdapter
import com.example.pppbuas.dashboard.DashboardActivity
import com.example.pppbuas.databinding.ActivityMainBinding
import com.example.pppbuas.util.FirestoreSeeder
import com.example.pppbuas.util.PrefManager
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity(), SignUpResultCallback, SignInResultCallback {
    private var authStateListener: FirebaseAuth.AuthStateListener? = null
    private lateinit var signInResultCallback: SignInResultCallback
    private lateinit var signUpResultCallback: SignUpResultCallback
    private lateinit var prefManager: PrefManager

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    companion object {
        lateinit var viewPager2: ViewPager2
        lateinit var authManager: AuthenticationManager
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        prefManager = PrefManager.getInstance(this)

        FirestoreSeeder.seed()

        signInResultCallback = this
        signUpResultCallback = this
        authManager = AuthenticationManager.getInstance(
            signUpResultCallback,
            signInResultCallback
        )


        authStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val currentUser = firebaseAuth.currentUser
            if (currentUser != null) {
                signInResultCallback.onSignInSuccess()
            }
        }
        FirebaseAuth.getInstance().addAuthStateListener(authStateListener!!)

        title = ""

        with(binding){
            viewPager.adapter = TabAdapter(this@MainActivity)
            viewPager2 = viewPager

            TabLayoutMediator(tabLayout, viewPager){
                    tab, position ->
                tab.text = when(position){
                    0 -> "Register"
                    1 -> "Login"
                    else -> ""
                }
            }.attach()

            tabLayout.isTabIndicatorFullWidth = true
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        FirebaseAuth.getInstance().removeAuthStateListener(authStateListener!!)
    }
    private fun navigateToDashboard() {
        val intent = Intent(this@MainActivity, DashboardActivity::class.java)
        startActivity(intent)
        finish()
    }
    override fun onSignUpSuccess() {
        prefManager.setLoggedIn(true)
        Toast.makeText(
            this@MainActivity,
            "Registered successfully",
            Toast.LENGTH_SHORT
        ).show()
        navigateToDashboard()
    }
    override fun onSignUpFailure(errorMessage: String?) {
        prefManager.setLoggedIn(false)
        Toast.makeText(this, "Register failed: $errorMessage", Toast.LENGTH_SHORT).show()
    }
    override fun onSignInSuccess() {
        prefManager.setLoggedIn(true)
        Toast.makeText(
            this@MainActivity,
            "Loged in successfully",
            Toast.LENGTH_SHORT
        ).show()
        navigateToDashboard()
    }
    override fun onSignInFailure(errorMessage: String?) {
        prefManager.setLoggedIn(false)
        Toast.makeText(this, "Login failed: $errorMessage", Toast.LENGTH_SHORT).show()
    }
}