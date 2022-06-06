package com.example.countries.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.countries.model.Countries
import com.example.countries.databinding.CountryItemBinding

class CountryAdapter(
    private val countriesData: MutableList<Countries> = mutableListOf()
) : RecyclerView.Adapter<CountryViewHolder>() {

    fun setNewCountries(newDataSet: List<Countries>) {
        countriesData.clear()
        countriesData.addAll(newDataSet)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder =
        CountryViewHolder(
            CountryItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) =
        holder.bind(countriesData[position])

    override fun getItemCount(): Int = countriesData.size
}

class CountryViewHolder(
    private val binding: CountryItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(countries: Countries) {
        binding.countryCode.text = countries.code
        binding.countryName.text = String.format(countries.name + ",")
        binding.countryRegion.text = countries.region
        binding.countryCapital.text = countries.capital
    }

}