package com.example.pppbuas.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pppbuas.databinding.ItemTicketBinding
import com.example.pppbuas.databinding.ItemTravelBinding
import com.example.pppbuas.model.Ticket
import com.example.pppbuas.model.Travel

typealias OnClickTicket = (Ticket) -> Unit
class TicketAdapter(private val listTicket : List<Ticket>, private val onClickMember: OnClickTicket)
    : RecyclerView.Adapter<TicketAdapter.ItemTicketViewHolder>()  {
    inner class ItemTicketViewHolder(private val binding : ItemTicketBinding)
        : RecyclerView.ViewHolder(binding.root) {
        fun bind(ticket : Ticket) {
            with(binding) {
                txtCityName.text = ticket.bookTimestamp.toString()

                itemView.setOnClickListener {
                    onClickMember(ticket)
                }
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemTicketViewHolder {
        val binding = ItemTicketBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemTicketViewHolder(binding)
    }
    override fun onBindViewHolder(holder: ItemTicketViewHolder, position: Int) {
        holder.bind(listTicket[position])
    }
    override fun getItemCount(): Int {
        return listTicket.size
    }
}