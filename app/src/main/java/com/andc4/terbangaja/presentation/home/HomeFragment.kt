package com.andc4.terbangaja.presentation.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.andc4.terbangaja.data.model.Airport
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
    private val searchAdapterFrom: RecentSearchAdapter by lazy {
        RecentSearchAdapter {
            insertData(1, it)
        }
    }

    private val searchAdapterTo: RecentSearchAdapter by lazy {
        RecentSearchAdapter {
            insertData(2, it)
        }
    }
    private var dataDestinationFrom: Airport? = null
    private var dataDestinationTo: Airport? = null

    private fun insertData(
        id: Int,
        airport: Airport,
    ) {
        when (id) {
            1 -> {
                binding.layoutHeader.layoutDestination.tvDestinationFrom.text = airport.city
                dataDestinationFrom = airport
            }

            2 -> {
                binding.layoutHeader.layoutDestination.tvDestinationTo.text = airport.city
                dataDestinationTo = airport
            }
        }
    }

    /* private val searchAdapter: RecentSearchAdapter by lazy {
         RecentSearchAdapter {
             insertData()
         }
     }*/
    private val favouriteDestinationAdapter: FavouriteDestinationAdapter by lazy {
        FavouriteDestinationAdapter {
        }
    }

    private fun bindFavouriteDestination(flight: List<Flight>) {
        favouriteDestinationAdapter.submitData(flight)
    }

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
            showBottomSheetDestinationFrom()
        }
        binding.layoutHeader.layoutDestination.tvDestinationTo.setOnClickListener {
            showBottomSheetDestinationTo()
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
        binding.layoutHeader.layoutDestination.icSwitch.setOnClickListener {
            switchDestination()
        }
    }

    private fun switchDestination() {
        if (dataDestinationFrom != null && dataDestinationTo != null) {
            val data = dataDestinationTo
            dataDestinationTo = dataDestinationFrom
            binding.layoutHeader.layoutDestination.tvDestinationTo.text = dataDestinationFrom?.city
            dataDestinationFrom = data
            binding.layoutHeader.layoutDestination.tvDestinationFrom.text = dataDestinationFrom?.city
        } else {
            Toast.makeText(requireContext(), "Isi data terlebih dahulu", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getDataRecentSearch() {
        viewModel.getRecentSearch().observe(viewLifecycleOwner) {
            it.proceedWhen(
                doOnSuccess = {
                    it.payload?.let {
                        // returnDataRecentSearch(it)
                    }
                },
            )
        }
    }

    private fun getDataFlight() {
        viewModel.getFLight().observe(viewLifecycleOwner) {
            it.proceedWhen(
                doOnLoading = {
                    binding.rvDestinationList.isVisible = false
                    binding.favouriteDestinationShimmer.isVisible = true
                    binding.shimmerFrameLayoutFavouriteDestination.startShimmer()
                },
                doOnSuccess = {
                    binding.rvDestinationList.isVisible = true
                    binding.shimmerFrameLayoutFavouriteDestination.isVisible = false
                    it.payload?.let {
                        bindFavouriteDestination(it)
                    }
                },
            )
        }
    }
    /*
        private fun returnDataRecentSearch(data: List<Destination>) {
            listSearch = data
        }*/

    private fun showBottomSheetDestinationFrom() {
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        val bottomSheetBinding = LayoutSheetDestinationBinding.inflate(layoutInflater)
        bottomSheetBinding.apply {
            bottomSheetBinding.recentSearchRecyclerview.apply {
                adapter = searchAdapterFrom
            }
            bottomSheetBinding.ivCross.setOnClickListener {
                bottomSheetDialog.dismiss()
            }
        }
        val searchView = bottomSheetBinding.searchView
        searchView.setOnQueryTextListener(
            object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(id: String?): Boolean {
                    return true
                }

                override fun onQueryTextChange(id: String?): Boolean {
                    if (id != null) {
                        viewModel.getAirportsById(id.toInt()).observe(viewLifecycleOwner) {
                            it.proceedWhen(
                                doOnSuccess = {
                                    bottomSheetBinding.recentSearchRecyclerview.isVisible = true
                                    it.payload?.let {
                                        val data = listOf(it, it, it)
                                        searchAdapterFrom.submitData(data)
                                    }
                                },
                                doOnError = {
                                    bottomSheetBinding.recentSearchRecyclerview.isVisible = false
                                },
                            )
                        }
                    } else {
                        bottomSheetBinding.recentSearchRecyclerview.isVisible = false
                    }
                    return true
                }
            },
        )
        bottomSheetDialog.setContentView(bottomSheetBinding.root)
        bottomSheetDialog.show()
    }

    private fun showBottomSheetDestinationTo() {
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        val bottomSheetBinding = LayoutSheetDestinationBinding.inflate(layoutInflater)
        bottomSheetBinding.apply {
            bottomSheetBinding.recentSearchRecyclerview.apply {
                adapter = searchAdapterTo
            }
            bottomSheetBinding.ivCross.setOnClickListener {
                bottomSheetDialog.dismiss()
            }
        }
        val searchView = bottomSheetBinding.searchView
        searchView.setOnQueryTextListener(
            object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(id: String?): Boolean {
                    return true
                }

                override fun onQueryTextChange(id: String?): Boolean {
                    if (id != null) {
                        viewModel.getAirportsById(id.toInt()).observe(viewLifecycleOwner) {
                            it.proceedWhen(
                                doOnSuccess = {
                                    bottomSheetBinding.recentSearchRecyclerview.isVisible = true
                                    it.payload?.let {
                                        val data = listOf(it, it, it)
                                        searchAdapterTo.submitData(data)
                                    }
                                },
                                doOnError = {
                                    bottomSheetBinding.recentSearchRecyclerview.isVisible = false
                                },
                            )
                        }
                    } else {
                        bottomSheetBinding.recentSearchRecyclerview.isVisible = false
                    }
                    return true
                }
            },
        )
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
