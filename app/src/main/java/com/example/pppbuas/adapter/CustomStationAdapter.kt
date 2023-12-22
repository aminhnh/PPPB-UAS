package com.example.pppbuas.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.pppbuas.R
import com.example.pppbuas.model.Station

class CustomStationAdapter<Station>(context: Context, private val stationList: List<Station>)
    : ArrayAdapter<Station>(context, R.layout.list_item, stationList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getCustomView(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getCustomView(position, convertView, parent)
    }

    private fun getCustomView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.list_item, parent, false)

        val station = getItem(position)

        // Set station name
//        view.findViewById<TextView>(R.id.textViewStationName).text = station?.name

        // Set city name (replace with your logic to get city name based on cityId)
//        view.findViewById<TextView>(R.id.textViewCityName).text = "City: " + getCityName(station?.cityId)

        return view
    }

    private fun getCityName(cityId: String?): String {
        return "City"
    }
}
