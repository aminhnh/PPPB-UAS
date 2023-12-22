package com.example.pppbuas.dashboard

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.pppbuas.R
import com.example.pppbuas.databinding.FragmentSearchBinding
import com.example.pppbuas.model.Station
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

/**
 * A simple [Fragment] subclass.
 * Use the [SearchFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SearchFragment : Fragment() {
    private var _binding : FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private val TAG = "SearchFragment"

    private val calendar = Calendar.getInstance()
    private var formattedDate : String = ""
    private var selectedDate = Calendar.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding){
            inputDate.setOnClickListener {
                showDatePicker()
            }
            var selectedTo:String? = null
            var selectedFrom:String? = null
            var selectedDate:String? = null

            findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<String?>("to")?.observe(viewLifecycleOwner) {
                result ->
                selectedTo = result
            }
            findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<String?>("from")?.observe(viewLifecycleOwner) {
                result ->
                selectedFrom = result
            }
            findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<String?>("date")?.observe(viewLifecycleOwner) {
                result ->
                selectedDate = result
            }
            setupAutoCompleteTextViewAndDate(selectedTo, selectedFrom, selectedDate)
        }
    }
    private fun setupAutoCompleteTextViewAndDate(selectedTo: String?, selectedFrom: String?, selectedDate: String?) {
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

                val positionTo = stationNames.indexOf(selectedTo)
                val positionFrom = stationNames.indexOf(selectedFrom)

                if (positionFrom != -1) {
                    autoCompleteTextViewFrom?.setText(stationNames[positionFrom], false)
                    // If you want to trigger the selection listener, uncomment the line below
//                     autoCompleteTextViewFrom?.listSelection = position
                }
                if (positionTo != -1) {
                    autoCompleteTextViewTo?.setText(stationNames[positionTo], false)
                    // If you want to trigger the selection listener, uncomment the line below
//                     autoCompleteTextViewFrom?.listSelection = position
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(requireContext(), "Error getting stations: ${exception.message}", Toast.LENGTH_SHORT).show()
            }

        if (selectedDate != null) {
            binding.inputDate.setText(selectedDate)
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