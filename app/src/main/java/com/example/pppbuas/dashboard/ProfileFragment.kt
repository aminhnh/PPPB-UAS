package com.example.pppbuas.dashboard

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.pppbuas.R
import com.example.pppbuas.auth.AuthenticationManager
import com.example.pppbuas.auth.SignInResultCallback
import com.example.pppbuas.auth.SignUpResultCallback
import com.example.pppbuas.databinding.FragmentProfileBinding
import com.example.pppbuas.user.AppUser
import com.example.pppbuas.user.UserManager
import com.google.firebase.auth.FirebaseAuth

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileFragment : Fragment() {
    private var _binding : FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val TAG = "ProfileFragment"
    private val viewModel: ProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)

            with(binding) {
                val currentUser = FirebaseAuth.getInstance().currentUser

                if (currentUser != null) {
                    // User is signed in
                    val userId = currentUser.uid

                    // Observe changes in userData and update UI accordingly
                    viewModel.userData.observe(viewLifecycleOwner) { userData ->
                        if (userData != null) {
                            accEmail.text = userData.email
                            accName.text = userData.fullName
                            accNim.text = userData.nim
                            accPhone.text = userData.phoneNumber
                        }
                    }

                    // Fetch user data only if it hasn't been fetched yet
                    if (viewModel.userData.value == null) {
                        viewModel.fetchUserData(userId)
                    }
                } else {
                    // User is not signed in
                    handleLogout()
                }

                btnLogout.setOnClickListener {
                    handleLogout()
                }
            }

    }
    private fun handleLogout() {
        val dummySignUpCallback = object : SignUpResultCallback {
            override fun onSignUpSuccess() {}
            override fun onSignUpFailure(errorMessage: String?) {}
        }
        val dummySignInCallback = object : SignInResultCallback {
            override fun onSignInSuccess() {}
            override fun onSignInFailure(errorMessage: String?) {}
        }
        AuthenticationManager.getInstance(dummySignUpCallback, dummySignInCallback).signOut()
        navigateToLoginScreen()
    }
    private fun navigateToLoginScreen() {
        findNavController().navigate(R.id.action_profileFragment_to_mainActivity)
    }
}