package com.example.pppbuas.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pppbuas.databinding.ItemRecommendedDestinationBinding
import com.example.pppbuas.model.City

typealias OnClickMember = (City) -> Unit
class RecommendedDestinationAdapter(private val listDestination : List<City>, private val onClickMember: OnClickMember)
    : RecyclerView.Adapter<RecommendedDestinationAdapter.ItemRecommendedDestinationViewHolder>()  {
    inner class ItemRecommendedDestinationViewHolder(private val binding : ItemRecommendedDestinationBinding)
        : RecyclerView.ViewHolder(binding.root) {
        fun bind(city : City) {
            with(binding) {
                Log.d("yes", "masuk binding");
                txtCityName.text = city.name

                city.image?.let {
                    imgBackground.setImageResource(it.toInt())
                }

//                Glide.with(binding.root.context).load(city.image).into(imgBackground)
//                imgBackground.setImageResource(R.drawable.new_image)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemRecommendedDestinationViewHolder {
        val binding = ItemRecommendedDestinationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemRecommendedDestinationViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return listDestination.size
    }

    override fun onBindViewHolder(holder: ItemRecommendedDestinationViewHolder, position: Int) {
        holder.bind(listDestination[position])
    }
}