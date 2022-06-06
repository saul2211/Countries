package com.example.countries.view

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.countries.Adapter.CountryAdapter
import com.example.countries.common.ResponseStatus
import com.example.countries.viewmodel.CountryViewModel
import com.example.countries.databinding.FragmentCountriesBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CountriesFragment : Fragment() {

    private val binding by lazy {
        FragmentCountriesBinding.inflate(layoutInflater)
    }

    private val countryAdapter by lazy {
        CountryAdapter()
    }

    private val countryViewModel: CountryViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding.countryRV.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = countryAdapter
        }

        countryViewModel.countries.observe(viewLifecycleOwner) { state ->
            when(state) {
                is ResponseStatus.LOADING -> {
                    binding.countryProgress.visibility = View.VISIBLE
                    binding.countryRV.visibility = View.GONE
                }
                is ResponseStatus.SUCCESS -> {
                    binding.countryProgress.visibility = View.GONE
                    binding.countryRV.visibility = View.VISIBLE
                    countryAdapter.setNewCountries(state.countries)
                }
                is ResponseStatus.ERROR -> {
                    binding.countryProgress.visibility = View.GONE
                    binding.countryRV.visibility = View.GONE

                    displayError(state.error.localizedMessage) {
                        countryViewModel.getAllCountries()
                    }
                }
            }
        }

        countryViewModel.getAllCountries()

        return binding.root
    }

    private fun displayError(message: String = "Working on the issues", retry: () -> Unit) {
        AlertDialog.Builder(requireContext())
            .setTitle("Error has occurred")
            .setPositiveButton("RETRY") { dialog, _ ->
                dialog.dismiss()
                retry()
            }
            .setNegativeButton("DISMISS") { dialog, _ ->
                dialog.dismiss()
            }
            .setMessage(message)
            .create()
            .show()
    }
}