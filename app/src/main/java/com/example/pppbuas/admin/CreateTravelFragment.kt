package com.example.pppbuas.admin

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.example.pppbuas.R
import com.example.pppbuas.dashboard.ProfileViewModel
import com.example.pppbuas.databinding.FragmentCreateOtherBinding
import com.example.pppbuas.databinding.FragmentCreateTravelBinding
import com.example.pppbuas.model.Station
import com.example.pppbuas.model.Travel
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

/**
 * A simple [Fragment] subclass.
 * Use the [CreateTravelFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CreateTravelFragment : Fragment() {
    private var _binding : FragmentCreateTravelBinding? = null
    private val binding get() = _binding!!
    private val TAG = "ProfileFragment"
    private val viewModel: ProfileViewModel by viewModels()

    private val calendar = Calendar.getInstance()
    private var formattedDate : String = ""
    private var selectedDate = Calendar.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCreateTravelBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            setupAutoCompleteTextView()
//            val items = listOf("Material", "Design", "Components", "Android")
//            val adapter = ArrayAdapter(requireContext(), R.layout.list_item, items)
//            (editTextFrom.editText as? AutoCompleteTextView)?.setAdapter(adapter)
//
//            val itemsTo = listOf("Material", "Design", "Components", "Android")
//            val adapterTo = ArrayAdapter(requireContext(), R.layout.list_item, itemsTo)
//            (editTextTo.editText as? AutoCompleteTextView)?.setAdapter(adapterTo)

            editTextDepartureTimeInput.setOnClickListener {
                showTimePickerDialog(editTextDepartureTimeInput)
            }

            editTextArrivalTimeInput.setOnClickListener {
                showTimePickerDialog(editTextArrivalTimeInput)
            }
            inputDate.setOnClickListener {
                showDatePicker()
            }

            btnCrete.setOnClickListener {
                saveTravelData()
            }
        }

    }
    private fun showTimePickerDialog(editText: EditText) {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            context,
            TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                val selectedTime = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute)
                editText.setText(selectedTime)
            },
            hour,
            minute,
            false
        )
        timePickerDialog.show()
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

    private fun saveTravelData() {
        val name = binding.inputName.text.toString().trim()
        val destinationStationId = binding.autoTvTo.text.toString().trim()
        val departureStationId = binding.autoTvFrom.text.toString().trim()
        val price = binding.editTextPriceInput.text.toString().toDoubleOrNull()
        val departureTime = binding.editTextDepartureTimeInput.text.toString().trim()
        val arrivalTime = binding.editTextArrivalTimeInput.text.toString().trim()
        val date = binding.inputDate.text.toString().trim()
        val totalSeats = binding.editTextTotalSeatsInput.text.toString().toIntOrNull()

        if (name.isNotBlank() && destinationStationId.isNotBlank() && departureStationId.isNotBlank() &&
            price != null && departureTime.isNotBlank() && arrivalTime.isNotBlank() &&
            date.isNotBlank() && totalSeats != null
        ) {
            val travel = Travel(
                name,
                destinationStationId,
                departureStationId,
                price,
                departureTime,
                arrivalTime,
                date,
                totalSeats,
                0 // Initialize bookedSeats to 0
            )

            firestore.collection("travels")
                .add(travel)
                .addOnSuccessListener {
                    Toast.makeText(requireContext(), "Travel created successfully", Toast.LENGTH_SHORT).show()
                    clearInputs()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(requireContext(), "Error creating travel: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupAutoCompleteTextView() {
        val autoCompleteTextViewFrom = binding.editTextFrom.editText as? AutoCompleteTextView
        val autoCompleteTextViewTo = binding.editTextTo.editText as? AutoCompleteTextView

        firestore.collection("stations")
            .get()
            .addOnSuccessListener { result ->
                val stationList = result.toObjects(Station::class.java)
                val stationNames = stationList.mapNotNull { it.name }

                // Create and set ArrayAdapter
                val adapter = ArrayAdapter(requireContext(), R.layout.list_item, stationNames)
                autoCompleteTextViewFrom?.setAdapter(adapter)
                autoCompleteTextViewTo?.setAdapter(adapter)
            }
            .addOnFailureListener { exception ->
                Toast.makeText(requireContext(), "Error getting stations: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }
    private fun clearInputs() {
        binding.inputName.text = null
        binding.editTextPriceInput.text = null
        binding.editTextDepartureTimeInput.text = null
        binding.editTextArrivalTimeInput.text = null
        binding.inputDate.text = null
        binding.editTextTotalSeatsInput.text = null

        binding.autoTvTo?.setText("", false)
        binding.autoTvFrom?.setText("", false)
    }
}