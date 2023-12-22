package com.example.pppbuas.ticket

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.pppbuas.dashboard.DashboardActivity
import com.example.pppbuas.databinding.FragmentBottomSheetBinding
import com.example.pppbuas.model.Travel
import com.example.pppbuas.room.MyExecutorService
import com.example.pppbuas.room.TravelDao
import com.example.pppbuas.room.TravelEntity
import com.example.pppbuas.room.TravelRoomDatabase
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.firestore.FirebaseFirestore
import java.util.concurrent.ExecutorService

/**
 * A simple [Fragment] subclass.
 * Use the [BottomSheetFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BottomSheetFragment : BottomSheetDialogFragment() {
    private var _binding: FragmentBottomSheetBinding? = null
    private val binding get() = _binding!!
    private val TAG = "BottomSheetFragment"
    private var travel: Travel = Travel()

    private lateinit var executorService : ExecutorService
    private lateinit var mTraveDao: TravelDao
    companion object {

        const val ARG_NAME = "arg_name"
        const val ARG_DESTINATION_STATION = "arg_destination_station"
        const val ARG_DEPARTURE_STATION = "arg_departure_station"
        const val ARG_PRICE = "arg_price"
        const val ARG_DEPARTURE_TIME = "arg_departure_time"
        const val ARG_ARRIVAL_TIME = "arg_arrival_time"
        const val ARG_DATE = "arg_date"
        const val ARG_TOTAL_SEATS = "arg_total_seats"
        const val ARG_BOOKED_SEATS = "arg_booked_seats"
        const val ARG_TRAVEL_ID = "arg_travel_id"

        fun newInstance(
            name: String?,
            destinationStation: String?,
            departureStation: String?,
            price: Double?,
            departureTime: String?,
            arrivalTime: String?,
            date: String?,
            totalSeats: Int?,
            bookedSeats: Int?,
            travelId: String?
        ): BottomSheetFragment {
            val fragment = BottomSheetFragment()
            val args = Bundle()
            args.putString(ARG_NAME, name)
            args.putString(ARG_DESTINATION_STATION, destinationStation)
            args.putString(ARG_DEPARTURE_STATION, departureStation)
            args.putDouble(ARG_PRICE, price ?: 0.0)
            args.putString(ARG_DEPARTURE_TIME, departureTime)
            args.putString(ARG_ARRIVAL_TIME, arrivalTime)
            args.putString(ARG_DATE, date)
            args.putInt(ARG_TOTAL_SEATS, totalSeats ?: 0)
            args.putInt(ARG_BOOKED_SEATS, bookedSeats ?: 0)
            args.putString(ARG_TRAVEL_ID, travelId)

            fragment.arguments = args
            return fragment
        }

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBottomSheetBinding.inflate(inflater, container, false)

        executorService = MyExecutorService.executorService
        val db = context?.let { TravelRoomDatabase.getDatabase(it) }
        mTraveDao = db!!.travelDao()!!


        with(binding){
            val name: String? by lazy { arguments?.getString(ARG_NAME) }
            val destinationStation: String? by lazy { arguments?.getString(ARG_DESTINATION_STATION) }
            val departureStation: String? by lazy { arguments?.getString(ARG_DEPARTURE_STATION) }
            val price: Double by lazy { arguments?.getDouble(ARG_PRICE) ?: 0.0 }
            val departureTime: String? by lazy { arguments?.getString(ARG_DEPARTURE_TIME) }
            val arrivalTime: String? by lazy { arguments?.getString(ARG_ARRIVAL_TIME) }
            val date: String? by lazy { arguments?.getString(ARG_DATE) }
            val totalSeats: Int by lazy { arguments?.getInt(ARG_TOTAL_SEATS) ?: 0 }
            val bookedSeats: Int by lazy { arguments?.getInt(ARG_BOOKED_SEATS) ?: 0 }
            val travelId: String? by lazy { arguments?.getString(ARG_TRAVEL_ID)}

            travelId?.let { getTravelById(it) }

            txtName.text = name

            btnContinue.setOnClickListener {
                BookActivity.travelId = travelId
                val intentToBookActivity = Intent(this@BottomSheetFragment.requireActivity(), BookActivity::class.java)
                startActivity(intentToBookActivity)
            }
            btnFav.setOnClickListener {
                val travelEntity = travelToTravelEntity(travel)
                insert(travelEntity)
            }

        }
        return binding.root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return BottomSheetDialog(requireContext())
    }
    private fun insert(travelEntity: TravelEntity) {
        executorService.execute { mTraveDao.insert(travelEntity) }
        Toast.makeText(context, "Travel added to favourites", Toast.LENGTH_SHORT).show()
    }
    private fun travelToTravelEntity(travel: Travel): TravelEntity {
        return TravelEntity(
            name = travel.name,
            destinationStation = travel.destinationStation,
            departureStation = travel.departureStation,
            price = travel.price,
            departureTime = travel.departureTime,
            arrivalTime = travel.arrivalTime,
            date = travel.date,
            totalSeats = travel.totalSeats,
            bookedSeats = travel.bookedSeats
        )
    }
    private fun getTravelById(id: String) {
        val travelsCollection = FirebaseFirestore.getInstance().collection("travels")

        travelsCollection.document(id)
            .get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    travel = documentSnapshot.toObject(Travel::class.java)!!
                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, exception.toString())
            }
    }
}