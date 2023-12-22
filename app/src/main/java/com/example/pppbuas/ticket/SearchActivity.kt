package com.example.pppbuas.ticket

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pppbuas.R
import com.example.pppbuas.adapter.TravelAdapter
import com.example.pppbuas.databinding.ActivitySearchBinding
import com.example.pppbuas.model.Station
import com.example.pppbuas.model.Travel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class SearchActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivitySearchBinding.inflate(layoutInflater)
    }
    private val TAG = "SearchActivity"
    private val calendar = Calendar.getInstance()
    private var formattedDate : String = ""
    private var selectedDate = Calendar.getInstance()
    private val firestore = FirebaseFirestore.getInstance()
    var adapterTravel : TravelAdapter = TravelAdapter(emptyList()) {}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        title = "Find a Ticket"
        with(binding) {

            rvTravels.layoutManager = LinearLayoutManager(this@SearchActivity)
            updateTravels()

            val toValue = intent.getStringExtra("to")
            val fromValue = intent.getStringExtra("from")
            val dateValue = intent.getStringExtra("date")

            setupAutoCompleteTextViewAndDate(toValue, fromValue, dateValue)

            inputDate.setOnClickListener {
                showDatePicker()
            }

            inputDate.addTextChangedListener (object: TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    updateTravels()
                }
                override fun afterTextChanged(s: Editable?) {
                }
            }
            )
            autoTvTo.addTextChangedListener ( object: TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    updateTravels()
                }
                override fun afterTextChanged(s: Editable?) {
                }
            }
            )
            autoTvFrom.addTextChangedListener ( object: TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    updateTravels()
                }
                override fun afterTextChanged(s: Editable?) {
                }
            }
            )
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
                val adapter = ArrayAdapter(this, R.layout.list_item, stationNames)
                autoCompleteTextViewFrom?.setAdapter(adapter)
                autoCompleteTextViewTo?.setAdapter(adapter)

                val positionTo = stationNames.indexOf(selectedTo)
                val positionFrom = stationNames.indexOf(selectedFrom)

                if (positionFrom != -1) {
                    autoCompleteTextViewFrom?.setText(stationNames[positionFrom], false)
                    // trigger the selection listener
                     autoCompleteTextViewFrom?.listSelection = positionFrom
                }
                if (positionTo != -1) {
                    autoCompleteTextViewTo?.setText(stationNames[positionTo], false)
                    // trigger the selection listener
                     autoCompleteTextViewFrom?.listSelection = positionTo
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Error getting stations: ${exception.message}", Toast.LENGTH_SHORT).show()
            }

        if (selectedDate!!.isNotEmpty()) {
            binding.inputDate.setText(selectedDate)
        }
    }
    private fun showDatePicker(){
        with(binding){
            val datePickerDialog = DatePickerDialog(this@SearchActivity, {DatePicker, year : Int, monthOfYear : Int, dayOfMonth : Int ->
                selectedDate.set(year, monthOfYear, dayOfMonth)
                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                formattedDate = dateFormat.format(selectedDate.time)
                inputDate.setText(formattedDate)
            },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            datePickerDialog.show()
        }
    }
    private fun updateTravels() {
        val collectionReference = firestore.collection("travels")

        val dateText = binding.inputDate.text.toString().trim()
        val destinationStationText = binding.autoTvTo.text.toString().trim()
        val departureStationText = binding.autoTvFrom.text.toString().trim()

        // Build the query dynamically based on the non-null values
        var query: Query = collectionReference

        if (!dateText.isNullOrEmpty()) {
            query = query.whereEqualTo("date", dateText)
        }

        if (!destinationStationText.isNullOrEmpty()) {
            query = query.whereEqualTo("destinationStation", destinationStationText)
        }

        if (!departureStationText.isNullOrEmpty()) {
            query = query.whereEqualTo("departureStation", departureStationText)
        }

        // Execute the query
        query.get()
            .addOnSuccessListener { documents: QuerySnapshot ->
                val travels = documents.map { it.toObject(Travel::class.java) }
                val travelIds = documents.map { it.id }

                val travelDataList = travels.zip(travelIds)

                adapterTravel = TravelAdapter(travels) { data ->
                    val documentId = travelDataList.find { it.first == data }?.second

                    val bottomSheet = BottomSheetFragment.newInstance(
                        name = data.name,
                        destinationStation = data.destinationStation,
                        departureStation = data.departureStation,
                        price = data.price,
                        departureTime = data.departureTime,
                        arrivalTime = data.arrivalTime,
                        date = data.date,
                        totalSeats = data.totalSeats,
                        bookedSeats = data.bookedSeats,
                        travelId = documentId
                    )
                    bottomSheet.show(supportFragmentManager, "ModalBottomSheet")
                }

                binding.rvTravels.adapter = adapterTravel
                binding.rvTravels.layoutManager = LinearLayoutManager(this@SearchActivity)

                if (documents.isEmpty) {
                    binding.txtAvailability.text = "No Trains Available"
                } else {
                    binding.txtAvailability.text = "Available Trains:"
                }
            }
            .addOnFailureListener { exception ->
                val travels = emptyList<Travel>()
                adapterTravel = TravelAdapter(travels){}
                binding.rvTravels.adapter = adapterTravel
                binding.rvTravels.layoutManager = LinearLayoutManager(this@SearchActivity)
                binding.txtAvailability.text = "No Trains Available"
            }
    }
}