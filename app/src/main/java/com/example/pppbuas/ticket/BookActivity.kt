package com.example.pppbuas.ticket

import android.content.Intent
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
import com.example.pppbuas.adapter.AmenityAdapter
import com.example.pppbuas.adapter.RecommendedDestinationAdapter
import com.example.pppbuas.dashboard.DashboardActivity
import com.example.pppbuas.databinding.ActivityBookBinding
import com.example.pppbuas.databinding.ActivitySearchBinding
import com.example.pppbuas.model.Amenity
import com.example.pppbuas.model.Station
import com.example.pppbuas.model.Ticket
import com.example.pppbuas.model.Travel
import com.example.pppbuas.model.TravelClass
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.util.Locale
import kotlin.time.times

class BookActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityBookBinding.inflate(layoutInflater)
    }
    private val TAG = "BookActivity"
    private val firestore = FirebaseFirestore.getInstance()
    private var travel: Travel? = Travel()
    private var travelClass: TravelClass? = TravelClass()
    var adapterAmenity : AmenityAdapter = AmenityAdapter(emptyList()) {}
    private val selectedAmenities = mutableListOf<Amenity>()
    private var totalPrice: Double = 0.0
    private var formattedTotalPrice: String = ""

    companion object {
        var travelId: String? = null
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        title = "Book a Ticket"
        setAmenities()

        with(binding) {
            getTravelById(travelId!!)
            setupAutoCompleteTextView()

            autoTvClass.addTextChangedListener (object: TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    updateClass(autoTvClass.text.toString())
                    updateTotalPrice()
                }
                override fun afterTextChanged(s: Editable?) {
                }
            }
            )
            editTextTotalSeatsInput.addTextChangedListener (object: TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    updateTotalPrice()
                }
                override fun afterTextChanged(s: Editable?) {
                }
            }
            )
            btnBook.setOnClickListener {
                bookTicket()
            }
        }
    }
    private fun bookTicket() {
        val travelId = travelId
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        var className: String? = null

        if (travelClass != null) {
            className = travelClass!!.name
        } else {
            className = "Economy Class"
        }

        var seatCount: Int = 1
        if (binding.editTextTotalSeatsInput.text.toString().isNotEmpty()) {
            seatCount = binding.editTextTotalSeatsInput.text.toString().toInt()
        }
        val bookTimestamp = System.currentTimeMillis()

        updateTotalPrice()
        val totalPrice = formattedTotalPrice

        val ticket = Ticket(travelId, userId, className, seatCount, bookTimestamp, totalPrice)
        saveTicketToFirestore(ticket)
    }
    private fun saveTicketToFirestore(ticket: Ticket) {
        val ticketsCollection = FirebaseFirestore.getInstance().collection("tickets")

        ticketsCollection
            .add(ticket)
            .addOnSuccessListener { documentReference ->
                Toast.makeText(this, "Ticket booked successfully", Toast.LENGTH_SHORT).show()
                Log.d("Firestore", "Ticket booked successfully with ID: ${documentReference.id}")
                navigateToDashboard()
            }
            .addOnFailureListener { exception ->
                // Handle failures, e.g., show an error message
                Log.e("Firestore", "Error booking ticket", exception)
                Toast.makeText(this, "Error booking ticket", Toast.LENGTH_SHORT).show()
            }
    }
    private fun navigateToDashboard() {
        val intent = Intent(this@BookActivity, DashboardActivity::class.java)
        startActivity(intent)
        finish()
    }
    private fun setAmenities() {
        firestore.collection("amenities")
            .get()
            .addOnSuccessListener { result ->
                val amenityList = result.toObjects(Amenity::class.java)
                adapterAmenity = AmenityAdapter(amenityList) {
                    clickedAmenity ->
                    toggleAmenitySelection(clickedAmenity)
                    updateTotalPrice()
                }

                binding.rvAmenity.apply {
                    adapter = adapterAmenity
                    layoutManager = LinearLayoutManager(context)
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Error getting stations: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }
    private fun toggleAmenitySelection(amenity: Amenity) {
        if (selectedAmenities.contains(amenity)) {
            selectedAmenities.remove(amenity)
            Toast.makeText(this, "Removed ${amenity.name}", Toast.LENGTH_SHORT).show()
        } else {
            selectedAmenities.add(amenity)
            Toast.makeText(this, "Added ${amenity.name} for $${amenity.price}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateTotalPrice() {
        val classPrice: Double = travelClass?.price ?: 1.0
        var seatCount: Int = 1
        if (binding.editTextTotalSeatsInput.text.toString().isNotEmpty()) {
            seatCount = binding.editTextTotalSeatsInput.text.toString().toInt()
        }
        totalPrice = ( (travel?.price ?: 0.0) + (selectedAmenities.sumOf { it.price ?: 0.0 } ) ).times(seatCount)
        if (classPrice != 0.0) {
            totalPrice += classPrice.times(seatCount)
        }
        formattedTotalPrice = String.format(Locale.US, "%.2f", totalPrice)

        binding.txtTotalPrice.text = "Total : $ $formattedTotalPrice"
    }
    private fun updateClass(name: String) {
        val travelClassesCollection = FirebaseFirestore.getInstance().collection("travelClasses")

        travelClassesCollection
            .whereEqualTo("name", name)
            .get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    val newTravelClass = documents.documents[0].toObject(TravelClass::class.java)
                    if (newTravelClass != null) {
                        travelClass = newTravelClass
                        Toast.makeText(this, "Choosen ${newTravelClass.name} for $${newTravelClass.price}", Toast.LENGTH_SHORT).show()
                    } else {
                    }
                } else {
                }
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Error getting TravelClass by name", exception)
            }
    }
    private fun getTravelById(id: String) {
        val travelsCollection = FirebaseFirestore.getInstance().collection("travels")

        travelsCollection.document(id)
            .get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    travel = documentSnapshot.toObject(Travel::class.java)
                    loadDataIntoView()
                    setInitialPrice()
                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, exception.toString())
            }
    }
    private fun setInitialPrice() {
        totalPrice = travel?.price?.toDouble() ?: 0.0
        binding.txtTotalPrice.text = "Total : $ $totalPrice"
        Toast.makeText(this, "Initial ticket price: $totalPrice", Toast.LENGTH_SHORT).show()
    }
    private fun loadDataIntoView() {
        with(binding) {
            txtName.text = travel?.name
            txtName.setText(travel?.name)
        }
    }
    private fun setupAutoCompleteTextView() {
        val autoCompleteTextViewFrom = binding.editTextClass.editText as? AutoCompleteTextView

        firestore.collection("travelClasses")
            .get()
            .addOnSuccessListener { result ->
                val classList = result.toObjects(TravelClass::class.java)
                val classNames = classList.mapNotNull { it.name }

                // Create and set ArrayAdapter
                val adapter = ArrayAdapter(this, R.layout.list_item, classNames)
                autoCompleteTextViewFrom?.setAdapter(adapter)
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Error getting stations: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }

}