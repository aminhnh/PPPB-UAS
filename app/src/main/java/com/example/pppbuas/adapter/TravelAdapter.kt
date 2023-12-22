package com.example.pppbuas.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pppbuas.databinding.ItemTravelBinding
import com.example.pppbuas.model.Travel

typealias OnClickMemberTravel = (Travel) -> Unit
class TravelAdapter(private val listDestination : List<Travel>, private val onClickMember: OnClickMemberTravel)
    : RecyclerView.Adapter<TravelAdapter.ItemTravelViewHolder>()  {
    inner class ItemTravelViewHolder(private val binding : ItemTravelBinding)
        : RecyclerView.ViewHolder(binding.root) {
        fun bind(travel : Travel) {
            with(binding) {
                txtCityName.text = travel.name

                itemView.setOnClickListener {
                    onClickMember(travel)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemTravelViewHolder {
        val binding = ItemTravelBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemTravelViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemTravelViewHolder, position: Int) {
        holder.bind(listDestination[position])
    }

    override fun getItemCount(): Int {
        return listDestination.size
    }
}