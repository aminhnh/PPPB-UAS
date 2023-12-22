package com.example.pppbuas.admin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.pppbuas.dashboard.ProfileViewModel
import com.example.pppbuas.databinding.FragmentCreateOtherBinding
import com.example.pppbuas.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth

/**
 * A simple [Fragment] subclass.
 * Use the [CreateOtherFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CreateOtherFragment : Fragment() {
    private var _binding : FragmentCreateOtherBinding? = null
    private val binding get() = _binding!!
    private val TAG = "CreateOtherFragment"
    private val viewModel: ProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCreateOtherBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
        }

    }
}