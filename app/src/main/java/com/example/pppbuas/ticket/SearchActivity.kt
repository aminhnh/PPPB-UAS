package com.example.pppbuas.ticket

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import com.example.pppbuas.R
import com.example.pppbuas.databinding.ActivityMainBinding
import com.example.pppbuas.databinding.ActivitySearchBinding
import com.example.pppbuas.model.Station
import com.google.firebase.firestore.FirebaseFirestore
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        with(binding) {
            val toValue = intent.getStringExtra("to")
            val fromValue = intent.getStringExtra("from")
            val dateValue = intent.getStringExtra("date")

            Log.d(TAG, "date :"+dateValue)
            setupAutoCompleteTextViewAndDate(toValue, fromValue, dateValue)

            inputDate.setOnClickListener {
                showDatePicker()
            }
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
                    // If you want to trigger the selection listener, uncomment the line below
                     autoCompleteTextViewFrom?.listSelection = positionFrom
                }
                if (positionTo != -1) {
                    autoCompleteTextViewTo?.setText(stationNames[positionTo], false)
                    // If you want to trigger the selection listener, uncomment the line below
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
}