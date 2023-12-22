package com.example.pppbuas.adapter

import com.example.pppbuas.room.TravelEntity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pppbuas.databinding.ItemTravelEntityBinding

typealias OnClickTravelEntity = (TravelEntity) -> Unit
class TravelEntityAdapter(private val listMember: List<TravelEntity>, private val onClickTravelEntity: OnClickTravelEntity)
    : RecyclerView.Adapter<TravelEntityAdapter.ItemTravelEntityViewHolder>() {
    inner class ItemTravelEntityViewHolder(private val binding: ItemTravelEntityBinding)
        : RecyclerView.ViewHolder(binding.root) {
        fun bind(travelEntity: TravelEntity) {
            with(binding) {
                txtName.text = travelEntity.name
                itemView.setOnClickListener {
                    onClickTravelEntity(travelEntity)
                }
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemTravelEntityViewHolder {
        val binding = ItemTravelEntityBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemTravelEntityViewHolder(binding)
    }
    override fun getItemCount(): Int {
        return listMember.size
    }
    override fun onBindViewHolder(holder: ItemTravelEntityViewHolder, position: Int) {
        holder.bind(listMember[position])
    }
}
