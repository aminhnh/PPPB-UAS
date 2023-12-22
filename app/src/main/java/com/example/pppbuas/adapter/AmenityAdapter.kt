package com.example.pppbuas.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.pppbuas.databinding.ItemAmenityBinding
import com.example.pppbuas.databinding.ItemTravelBinding
import com.example.pppbuas.model.Amenity
import com.example.pppbuas.model.Travel

typealias OnClickAmenity = (Amenity) -> Unit
class AmenityAdapter(private val listDestination : List<Amenity>, private val onClickMember: OnClickAmenity)
    : RecyclerView.Adapter<AmenityAdapter.ItemAmenityViewHolder>()  {
    inner class ItemAmenityViewHolder(private val binding : ItemAmenityBinding)
        : RecyclerView.ViewHolder(binding.root) {
        fun bind(amenity : Amenity) {
            with(binding) {
                txtAmenityName.text = amenity.name
                txtAmenityDesc.text = amenity.description
                txtAmenityPrice.text = amenity.price.toString()

                itemView.setOnClickListener {
                    onClickMember(amenity)
                    imgCheck.isVisible = !imgCheck.isVisible
                    imgBottomBar.isVisible = !imgBottomBar.isVisible
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemAmenityViewHolder {
        val binding = ItemAmenityBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemAmenityViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemAmenityViewHolder, position: Int) {
        holder.bind(listDestination[position])
    }

    override fun getItemCount(): Int {
        return listDestination.size
    }
}