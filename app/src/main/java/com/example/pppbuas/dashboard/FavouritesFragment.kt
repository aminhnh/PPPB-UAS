package com.example.pppbuas.dashboard

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pppbuas.R
import com.example.pppbuas.adapter.TicketAdapter
import com.example.pppbuas.adapter.TravelAdapter
import com.example.pppbuas.adapter.TravelEntityAdapter
import com.example.pppbuas.databinding.FragmentFavouritesBinding
import com.example.pppbuas.databinding.FragmentHistoryBinding
import com.example.pppbuas.model.Ticket
import com.example.pppbuas.room.MyExecutorService
import com.example.pppbuas.room.TravelDao
import com.example.pppbuas.room.TravelEntity
import com.example.pppbuas.room.TravelRoomDatabase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.concurrent.ExecutorService

/**
 * A simple [Fragment] subclass.
 * Use the [FavouritesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FavouritesFragment : Fragment() {
    private var _binding : FragmentFavouritesBinding? = null
    private val binding get() = _binding!!
    private val TAG = "FavouritesFragment"
    private lateinit var executorService: ExecutorService
    private lateinit var mTravelEntitiesDao: TravelDao

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,    
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavouritesBinding.inflate(inflater, container, false)
        return binding.root
    }
//    private fun getAllNotes() {
//        mTravelDao.allTravels.observe(viewLifecycleOwner) { travels ->
//            val adapterTravel = TravelAdapter(travels) { travelEntity ->
//                val travel: TravelEntity = travelEntityToTravel(travelEntity)
//                // Now you can use the 'travel' object as needed
//            }
//
//            binding.rvTickets.apply {
//                adapter = adapterTravel
//                layoutManager = LinearLayoutManager(requireContext())
//            }
//        }
//    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        executorService = MyExecutorService.executorService

        val db = TravelRoomDatabase.getDatabase(requireContext())
        mTravelEntitiesDao = db.travelDao()

        getAllTravelEntities()
    }

    private fun getAllTravelEntities(keyword: String = "") {
        mTravelEntitiesDao.allTravels.observe(viewLifecycleOwner) { travelEntities ->
            val adapterTravelEntity = TravelEntityAdapter(travelEntities) { travelEntity ->
                removeFav(travelEntity)
            }

            binding.rvTickets.apply {
                adapter = adapterTravelEntity
                layoutManager = LinearLayoutManager(requireContext())
            }
        }
    }
    private fun removeFav(travelEntity: TravelEntity) {
        executorService.execute { mTravelEntitiesDao.delete(travelEntity) }
        Toast.makeText(context, "Travel deleted from favourites", Toast.LENGTH_SHORT).show()
    }
    override fun onResume() {
        super.onResume()
        getAllTravelEntities()
    }

}