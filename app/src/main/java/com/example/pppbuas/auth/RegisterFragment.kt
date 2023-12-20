package com.example.pppbuas.auth

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.pppbuas.MainActivity
import com.example.pppbuas.databinding.FragmentRegisterBinding

/**
 * A simple [Fragment] subclass.
 * Use the [RegisterFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RegisterFragment : Fragment() {
    private var _binding : FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private val TAG = "RegisterFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            btnRegister.setOnClickListener {
                if (validateInput()) {
                    val email = inputEmail.text.toString()
                    val password = inputPassword.text.toString()
                    val fullName = inputName.text.toString()
                    val phoneNumber = inputPhone.text.toString()
                    val nim = inputNim.text.toString()

                    MainActivity.authManager.signUpUser(email = email, password = password, fullName = fullName, phoneNumber = phoneNumber, nim = nim)
                }
            }
        }

    }
    private fun validateInput(): Boolean {
        // TODO: implement
        return true
    }
    override fun onDestroy() {
        super.onDestroy()
    }
}