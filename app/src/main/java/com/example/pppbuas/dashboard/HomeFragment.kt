package com.example.pppbuas.dashboard

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pppbuas.R
import com.example.pppbuas.ticket.SearchActivity
import com.example.pppbuas.model.City
import com.example.pppbuas.adapter.RecommendedDestinationAdapter
import com.example.pppbuas.databinding.FragmentHomeBinding
import com.example.pppbuas.model.Station
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    private var _binding : FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val TAG = "HomeFragment"
    var adapterDestination : RecommendedDestinationAdapter = RecommendedDestinationAdapter(emptyList()) {}

    private val calendar = Calendar.getInstance()
    private var formattedDate : String = ""
    private var selectedDate = Calendar.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

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
            setupAutoCompleteTextView()
            rvDestination.apply {
                adapter = adapterDestination
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            }
            inputDate.setOnClickListener {
                showDatePicker()
            }
            btnSearch.setOnClickListener {
                val date: String = inputDate.text.toString().trim()
                val to: String = autoTvTo.text.toString().trim()
                val from: String = autoTvFrom.text.toString().trim()

                val intentToSearch = Intent(requireContext(), SearchActivity::class.java)
                intentToSearch.putExtra("date", date)
                intentToSearch.putExtra("to", to)
                intentToSearch.putExtra("from", from)
                startActivity(intentToSearch)
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
    private fun setupAutoCompleteTextView() {
        val autoCompleteTextViewFrom = binding.editTextFrom.editText as? AutoCompleteTextView
        val autoCompleteTextViewTo = binding.editTextTo.editText as? AutoCompleteTextView

        firestore.collection("stations")
            .get()
            .addOnSuccessListener { result ->
                val stationList = result.toObjects(Station::class.java)
                val stationNames = stationList.mapNotNull { it.name + ", " + it.city }

                // Create and set ArrayAdapter
                val adapter = ArrayAdapter(requireContext(), R.layout.list_item, stationNames)
                autoCompleteTextViewFrom?.setAdapter(adapter)
                autoCompleteTextViewTo?.setAdapter(adapter)
            }
            .addOnFailureListener { exception ->
                Toast.makeText(requireContext(), "Error getting stations: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }
    private fun showDatePicker(){
        with(binding){
            val datePickerDialog =
                context?.let {
                    DatePickerDialog(
                        it, { DatePicker, year : Int, monthOfYear : Int, dayOfMonth : Int ->
                            selectedDate.set(year, monthOfYear, dayOfMonth)
                            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                            formattedDate = dateFormat.format(selectedDate.time)
                            inputDate.setText(formattedDate)
                        },
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)
                    )
                }
            datePickerDialog?.show()
        }
    }
}