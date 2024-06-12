package com.andc4.terbangaja.presentation.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.andc4.terbangaja.data.model.Destination
import com.andc4.terbangaja.data.model.Flight
import com.andc4.terbangaja.databinding.FragmentHomeBinding
import com.andc4.terbangaja.databinding.LayoutSheetDestinationBinding
import com.andc4.terbangaja.databinding.LayoutSheetFlightTypeBinding
import com.andc4.terbangaja.databinding.LayoutSheetPassengerCountBinding
import com.andc4.terbangaja.presentation.home.adapter.FavouriteDestinationAdapter
import com.andc4.terbangaja.presentation.home.adapter.RecentSearchAdapter
import com.andc4.terbangaja.presentation.seat.SeatActivity
import com.andc4.terbangaja.utils.proceedWhen
import com.google.android.material.bottomsheet.BottomSheetDialog
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeViewModel by viewModel()
    private val searchAdapter: RecentSearchAdapter by lazy {
        RecentSearchAdapter {
        }
    }
    private val favouriteDestinationAdapter: FavouriteDestinationAdapter by lazy {
        FavouriteDestinationAdapter {
        }
    }

    private fun bindFavouriteDestination(flight: List<Flight>) {
        favouriteDestinationAdapter.submitData(flight)
    }

    private var listSearch: List<Destination> =
        listOf(Destination(0, "Jakarta"), Destination(1, "Surabaya"))

    private var passengerCount: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        getDataRecentSearch()
        getDataFlight()
        setUpDataFLight()
        setOnClick()
    }

    private fun setUpDataFLight() {
        binding.rvDestinationList.apply {
            adapter = favouriteDestinationAdapter
        }
    }

    private fun setOnClick() {
        binding.layoutHeader.layoutDestination.tvDestinationFrom.setOnClickListener {
            searchAdapter.submitData(listSearch)
            showBottomSheetDestinationFrom(listSearch)
        }
        binding.layoutHeader.layoutDestination.tvDestinationTo.setOnClickListener {
            searchAdapter.submitData(listSearch)
            showBottomSheetDestinationTo(listSearch)
        }
        binding.layoutHeader.tvPassengersCount.setOnClickListener {
            showBottomSheetPassengerCount()
        }
        binding.layoutHeader.tvSeatClassType.setOnClickListener {
            showBottomSheetPassengerClass()
        }
        binding.layoutHeader.btnSearch.setOnClickListener {
            startActivity(Intent(requireContext(), SeatActivity::class.java))
        }
    }

    private fun getDataRecentSearch() {
        viewModel.getRecentSearch().observe(viewLifecycleOwner) {
            it.proceedWhen(
                doOnSuccess = {
                    it.payload?.let {
                        returnDataRecentSearch(it)
                    }
                },
            )
        }
    }

    private fun getDataFlight() {
        viewModel.getFLight().observe(viewLifecycleOwner) {
            it.proceedWhen(
                doOnSuccess = {
                    it.payload?.let {
                        bindFavouriteDestination(it)
                    }
                },
            )
        }
    }

    private fun returnDataRecentSearch(data: List<Destination>) {
        listSearch = data
    }

    private fun showBottomSheetDestinationFrom(data: List<Destination>?) {
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        val bottomSheetBinding = LayoutSheetDestinationBinding.inflate(layoutInflater)
        bottomSheetBinding.apply {
            bottomSheetBinding.recentSearchRecyclerview.apply {
                adapter = searchAdapter
            }
            bottomSheetBinding.ivCross.setOnClickListener {
                bottomSheetDialog.dismiss()
            }
        }
        bottomSheetDialog.setContentView(bottomSheetBinding.root)
        bottomSheetDialog.show()
    }

    private fun showBottomSheetDestinationTo(data: List<Destination>?) {
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        val bottomSheetBinding = LayoutSheetDestinationBinding.inflate(layoutInflater)
        bottomSheetBinding.apply {
            bottomSheetBinding.recentSearchRecyclerview.apply {
                adapter = searchAdapter
            }
            bottomSheetBinding.ivCross.setOnClickListener {
                bottomSheetDialog.dismiss()
            }
        }
        bottomSheetDialog.setContentView(bottomSheetBinding.root)
        bottomSheetDialog.show()
    }

    private fun showBottomSheetPassengerCount() {
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        val bottomSheetBinding = LayoutSheetPassengerCountBinding.inflate(layoutInflater)
        bottomSheetBinding.apply {
            bottomSheetBinding.ivCross.setOnClickListener {
                bottomSheetDialog.dismiss()
            }
        }
        bottomSheetDialog.setContentView(bottomSheetBinding.root)
        bottomSheetDialog.show()
    }

    private fun showBottomSheetPassengerClass() {
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        val bottomSheetBinding = LayoutSheetFlightTypeBinding.inflate(layoutInflater)
        bottomSheetBinding.apply {
            bottomSheetBinding.ivCross.setOnClickListener {
                bottomSheetDialog.dismiss()
            }
        }
        bottomSheetDialog.setContentView(bottomSheetBinding.root)
        bottomSheetDialog.show()
    }
}
