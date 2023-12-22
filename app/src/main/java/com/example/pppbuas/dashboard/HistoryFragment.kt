package com.example.pppbuas.dashboard

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pppbuas.R
import com.example.pppbuas.adapter.RecommendedDestinationAdapter
import com.example.pppbuas.adapter.TicketAdapter
import com.example.pppbuas.adapter.TravelAdapter
import com.example.pppbuas.databinding.FragmentHistoryBinding
import com.example.pppbuas.databinding.FragmentHomeBinding
import com.example.pppbuas.model.Ticket
import com.example.pppbuas.ticket.SearchActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

/**
 * A simple [Fragment] subclass.
 * Use the [HistoryFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HistoryFragment : Fragment() {
    private var _binding : FragmentHistoryBinding? = null
    private val binding get() = _binding!!
    private val TAG = "HistoryFragment"
    var adapterTicket : TicketAdapter = TicketAdapter(emptyList()) {}
    val firestore = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        updateHistory()
        with(binding) {

        }

    }
    private fun updateHistory() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        val ticketsCollection = firestore.collection("tickets")

        ticketsCollection
            .whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener { documents ->
                val tickets = documents.mapNotNull { it.toObject(Ticket::class.java) }

                adapterTicket = TicketAdapter(tickets) {
                    data ->
                    // ON CLICK
                }
                with(binding) {
                    rvTickets.apply {
                        adapter = adapterTicket
                        layoutManager = LinearLayoutManager(context)
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, exception.toString())
            }
    }
}