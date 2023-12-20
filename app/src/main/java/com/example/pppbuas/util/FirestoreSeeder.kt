package com.example.pppbuas.util

import com.example.pppbuas.model.Amenity
import com.example.pppbuas.model.TravelClass
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

object FirestoreSeeder {

    private val firestore = FirebaseFirestore.getInstance()

    fun seed() {
//        seedCities()
//        seedStations()
//        seedAmenities()
//        seedTravelClasses()
    }
    private fun seedTravelClasses() {
        val travelClasses = listOf(
            TravelClass("Economy", "Standard seating and services", 0.0),
            TravelClass("Business", "Enhanced seating and services", 100.0),
            TravelClass("First Class", "Luxurious seating and premium services", 200.0)
        )

        GlobalScope.launch {
            for (travelClass in travelClasses) {
                try {
                    firestore.collection("travelClasses").add(travelClass).await()
                    println("Travel class added: ${travelClass.name}")
                } catch (e: Exception) {
                    println("Error adding travel class: ${travelClass.name}, ${e.message}")
                }
            }
        }
    }
    private fun seedAmenities() {
        val amenities = listOf(
            Amenity("Swimming Pool", "Relax by the poolside", 10.99),
            Amenity("Gym", "Stay fit with our state-of-the-art gym", 5.99),
            Amenity("Special Assistance", "Services for passengers with reduced mobility, disabilities, or other special needs", 0.0),
            Amenity("Power Outlets", "Availability of power outlets to charge electronic devices", 2.99),
            Amenity("Seat Selection", "The ability to choose specific seats, such as window or aisle seats", 3.99),
            Amenity("Spa", "Indulge in a rejuvenating spa experience", 15.99),
            Amenity("Meal Services", "Indulge in full course meal", 12.99),
            Amenity("Wi-Fi Access", "Stay connected during the journey", 4.99),
            Amenity("Lounge Access", "Access to lounges at train stations for relaxation and refreshments before departure", 8.99),
            Amenity("Entertainment", "On-board entertainment systems, such as screens for movies, music, or games", 6.99)
        )

        GlobalScope.launch {
            for (amenity in amenities) {
                try {
                    firestore.collection("amenities").add(amenity).await()
                    println("Amenity added: ${amenity.name}")
                } catch (e: Exception) {
                    println("Error adding amenity: ${amenity.name}, ${e.message}")
                }
            }
        }
    }
    private fun seedCities() {
        val cities = listOf("CityA", "CityB", "CityC")

        GlobalScope.launch {
            for (cityName in cities) {
                val cityData = hashMapOf("name" to cityName)
                firestore.collection("cities").add(cityData)
                    .addOnSuccessListener { documentReference ->
                        println("City added with ID: ${documentReference.id}")
                    }
                    .addOnFailureListener { e ->
                        println("Error adding city: $e")
                    }
            }
        }
    }

    private fun seedStations() {
        val stations = listOf(
            Pair("StationA", "Lh6Eg3WCYDLO7ZT2ocLe"),
            Pair("StationB", "PY5UvHXHX41SFeOn3Cgz"),
            Pair("StationC", "PY5UvHXHX41SFeOn3Cgz"),
            Pair("StationD", "nmai3qD8kgq1kCrTBil1")
        )

        GlobalScope.launch {
            for ((stationName, cityId) in stations) {
                val stationData = hashMapOf(
                    "name" to stationName,
                    "cityId" to cityId
                )
                firestore.collection("stations").add(stationData)
                    .addOnSuccessListener { documentReference ->
                        println("Station added with ID: ${documentReference.id}")
                    }
                    .addOnFailureListener { e ->
                        println("Error adding station: $e")
                    }
            }
        }
    }
}
