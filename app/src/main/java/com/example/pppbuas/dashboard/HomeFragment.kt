package com.example.pppbuas.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pppbuas.R
import com.example.pppbuas.databinding.FragmentHomeBinding
import com.example.pppbuas.model.City
import com.example.pppbuas.adapter.RecommendedDestinationAdapter

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    private var _binding : FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val TAG = "HomeFragment"
    var adapterDestination : RecommendedDestinationAdapter = RecommendedDestinationAdapter(emptyList()) {}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val destinations = getRecommendedDestinations()

        adapterDestination = RecommendedDestinationAdapter(destinations) {
            data ->

        }

        with(binding){
            rvDestination.apply {
                adapter = adapterDestination
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            }
        }

    }
    private fun getRecommendedDestinations(): List<City>{
        return listOf(
            City("Tokyo", R.drawable.img_city_tokyo.toString()),
            City("Kyoto", R.drawable.img_city_kyoto.toString()),
            City("Hiroshima", R.drawable.img_city_hiroshima.toString()),
            City("Osaka", R.drawable.img_city_osaka.toString()),
            City("Sapporo", R.drawable.img_city_sapporo.toString())
        )
    }
}