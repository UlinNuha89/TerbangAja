package com.andc4.terbangaja.presentation.seat

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.andc4.terbangaja.R
import com.andc4.terbangaja.data.mapper.toSeatsString
import com.andc4.terbangaja.data.mapper.toTitleString
import com.andc4.terbangaja.data.model.Flight
import com.andc4.terbangaja.data.model.SeatClass
import com.andc4.terbangaja.data.model.Seats
import com.andc4.terbangaja.data.model.UserTicket
import com.andc4.terbangaja.databinding.ActivitySeatBinding
import com.andc4.terbangaja.presentation.checkout.CheckoutActivity
import com.andc4.terbangaja.utils.proceedWhen
import dev.jahidhasanco.seatbookview.SeatBookView
import dev.jahidhasanco.seatbookview.SeatClickListener
import dev.jahidhasanco.seatbookview.SeatLongClickListener
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class SeatActivity : AppCompatActivity() {
    private lateinit var seatBookViewFirst: SeatBookView
    private lateinit var seatBookViewSecond: SeatBookView
    private val binding: ActivitySeatBinding by lazy {
        ActivitySeatBinding.inflate(layoutInflater)
    }
    private val viewModel: SeatViewModel by viewModel {
        parametersOf(intent.extras)
    }
    private var seatCount: Int = 1

    private var dataDepartureFlight: Flight? = null
    private var dataReturnFlight: Flight? = null
    private var dataSeatClass: SeatClass? = null
    private var seats: String? = null
    private var selectedSeatFirst: List<Seats>? = null
    private var selectedSeatSecond: List<Seats>? = null
    private var title: List<String>? = null

    companion object {
        const val EXTRAS_USER_TICKET = "EXTRAS_USER_TICKET"
        const val EXTRAS_IS_ROUND_TRIP = "EXTRAS_IS_ROUND_TRIP"

        fun startActivity(
            context: Context,
            userTicket: UserTicket,
            isRoundTrip: Boolean,
        ) {
            val intent = Intent(context, SeatActivity::class.java)
            intent.putExtra(EXTRAS_USER_TICKET, userTicket)
            intent.putExtra(EXTRAS_IS_ROUND_TRIP, isRoundTrip)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        getData()
        setOnClick()
    }

    private fun getData() {
        val data = viewModel.getDataUserTicket()!!
        dataSeatClass = data.seatClass
        seatCount = data.passenger.adult + data.passenger.children

        binding.tvSubTitle.text = dataSeatClass?.name
        if (viewModel.isRoundTrip()) {
            dataDepartureFlight = data.departureFlight
            dataReturnFlight = data.returnFlight
            binding.cvSeatFirst.isVisible = true
            binding.cvSeatSecond.isVisible = true
            getDataSeatDeparture()
            getDataSeatReturn()
        } else {
            dataDepartureFlight = data.departureFlight
            binding.cvSeatFirst.isVisible = true
            binding.shimmerFrameSeatSecond.isVisible = false
            binding.cvSeatSecond.isVisible = false
            getDataSeatDeparture()
        }
    }

    private fun getDataSeatDeparture() {
        viewModel.getSeatList(dataDepartureFlight!!.id, dataSeatClass!!.value).observe(this) {
            it.proceedWhen(
                doOnSuccess = {
                    binding.cvSeatFirst.isVisible = true
                    binding.shimmerFrameSeatFirst.isVisible = false
                    binding.tvError.isVisible = false
                    it.payload?.let {
                        seats = it.toSeatsString()
                        title = it.toTitleString()
                        binding.tvCardTitleFirst.text =
                            getString(
                                R.string.seats_available,
                                dataSeatClass!!.name,
                                it.size.toString(),
                            )
                        viewModel.setDepartureSeatsData(it)
                    }
                    setDepartureSeatView()
                },
                doOnError = {
                    binding.cvSeatFirst.isVisible = false
                    binding.shimmerFrameSeatFirst.isVisible = false
                    binding.tvError.isVisible = true
                    binding.tvError.text = it.exception?.cause?.message
                },
                doOnLoading = {
                    binding.cvSeatFirst.isVisible = false
                    binding.shimmerFrameSeatFirst.isVisible = true
                    binding.tvError.isVisible = false
                    binding.shimmerFrameSeatFirst.startShimmer()
                },
                doOnEmpty = {
                    if (it.payload?.isEmpty() == true) {
                        binding.cvSeatFirst.isVisible = false
                        binding.shimmerFrameSeatFirst.isVisible = false
                        binding.tvError.isVisible = true
                        binding.tvError.text = getString(R.string.text_empty_data)
                    }
                },
            )
        }
    }

    private fun getDataSeatReturn() {
        viewModel.getSeatList(dataReturnFlight!!.id, dataSeatClass!!.value).observe(this) {
            it.proceedWhen(
                doOnSuccess = {
                    binding.cvSeatSecond.isVisible = true
                    binding.shimmerFrameSeatSecond.isVisible = false
                    binding.tvError.isVisible = false
                    it.payload?.let {
                        seats = it.toSeatsString()
                        title = it.toTitleString()
                        binding.tvCardTitleSecond.text =
                            getString(
                                R.string.seats_available,
                                dataSeatClass!!.name,
                                it.size.toString(),
                            )
                        viewModel.setReturnSeatsData(it)
                    }
                    setReturnSeatView()
                },
                doOnError = {
                    binding.cvSeatSecond.isVisible = false
                    binding.shimmerFrameSeatSecond.isVisible = false
                    binding.tvError.isVisible = true
                    binding.tvError.text = it.exception?.cause?.message
                },
                doOnLoading = {
                    binding.cvSeatSecond.isVisible = false
                    binding.shimmerFrameSeatSecond.isVisible = true
                    binding.tvError.isVisible = false
                    binding.shimmerFrameSeatSecond.startShimmer()
                },
                doOnEmpty = {
                    if (it.payload?.isEmpty() == true) {
                        binding.cvSeatSecond.isVisible = false
                        binding.shimmerFrameSeatSecond.isVisible = false
                        binding.tvError.isVisible = true
                        binding.tvError.text = getString(R.string.text_empty_data)
                    }
                },
            )
        }
    }

    private fun setDepartureSeatView() {
        seatBookViewFirst = binding.layoutSeatFirst
        seatBookViewFirst.setSeatsLayoutString(seats!!)
            .isCustomTitle(true)
            .setCustomTitle(title!!)
            .setSeatLayoutPadding(2)
            .setSelectSeatLimit(seatCount)
            .setSeatSizeBySeatsColumnAndLayoutWidth(7, -1)
        seatBookViewFirst.show()

        seatBookViewFirst.setSeatClickListener(
            object : SeatClickListener {
                override fun onAvailableSeatClick(
                    selectedIdList: List<Int>,
                    view: View,
                ) {
                    selectedSeatFirst = viewModel.getDepartureSelectedSeatObject(selectedIdList)
                }

                override fun onBookedSeatClick(view: View) {
                    Toast.makeText(
                        this@SeatActivity,
                        getString(R.string.seat_is_booked),
                        Toast.LENGTH_SHORT,
                    ).show()
                }

                override fun onReservedSeatClick(view: View) {
                    // Handle reserved seat click
                }
            },
        )
        seatBookViewFirst.setSeatLongClickListener(
            object : SeatLongClickListener {
                override fun onAvailableSeatLongClick(view: View) {
                    val data = view.id.toString()
                    Toast.makeText(
                        this@SeatActivity,
                        getString(R.string.seat_available, data),
                        Toast.LENGTH_SHORT,
                    ).show()
                }

                override fun onBookedSeatLongClick(view: View) {
                    val data = view.id.toString()
                    Toast.makeText(
                        this@SeatActivity,
                        getString(R.string.seat_booked, data),
                        Toast.LENGTH_SHORT,
                    ).show()
                }

                override fun onReservedSeatLongClick(view: View) {
                    // Handle reserved seat long click
                }
            },
        )
    }

    private fun setReturnSeatView() {
        seatBookViewSecond = binding.layoutSeatSecond
        seatBookViewSecond.setSeatsLayoutString(seats!!)
            .isCustomTitle(true)
            .setCustomTitle(title!!)
            .setSeatLayoutPadding(2)
            .setSelectSeatLimit(seatCount)
            .setSeatSizeBySeatsColumnAndLayoutWidth(7, -1)
        seatBookViewSecond.show()

        seatBookViewSecond.setSeatClickListener(
            object : SeatClickListener {
                override fun onAvailableSeatClick(
                    selectedIdList: List<Int>,
                    view: View,
                ) {
                    selectedSeatSecond = viewModel.getReturnSelectedSeatObject(selectedIdList)
                }

                override fun onBookedSeatClick(view: View) {
                    Toast.makeText(this@SeatActivity, getString(R.string.seat_is_booked), Toast.LENGTH_SHORT).show()
                }

                override fun onReservedSeatClick(view: View) {
                    // Handle reserved seat click
                }
            },
        )
        seatBookViewSecond.setSeatLongClickListener(
            object : SeatLongClickListener {
                override fun onAvailableSeatLongClick(view: View) {
                    val data = view.id.toString()
                    Toast.makeText(
                        this@SeatActivity,
                        getString(R.string.seat_available, data),
                        Toast.LENGTH_SHORT,
                    ).show()
                }

                override fun onBookedSeatLongClick(view: View) {
                    val data = view.id.toString()
                    Toast.makeText(
                        this@SeatActivity,
                        getString(R.string.seat_booked, data),
                        Toast.LENGTH_SHORT,
                    ).show()
                }

                override fun onReservedSeatLongClick(view: View) {
                    // Handle reserved seat long click
                }
            },
        )
    }

    private fun setOnClick() {
        binding.btnSave.setOnClickListener {
            if (viewModel.isRoundTrip()) {
                if (selectedSeatFirst?.size == seatCount && selectedSeatSecond?.size == seatCount) {
                    navToCheckout()
                } else {
                    Toast.makeText(this, getString(R.string.silahkan_pilih_seat), Toast.LENGTH_SHORT).show()
                }
            } else {
                if (selectedSeatFirst?.size == seatCount) {
                    navToCheckout()
                } else {
                    Toast.makeText(this, getString(R.string.silahkan_pilih_seat), Toast.LENGTH_SHORT).show()
                }
            }
        }
        binding.ivBackArrow.setOnClickListener {
            onBackPressed()
        }
    }

    private fun navToCheckout() {
        val data = viewModel.getDataUserTicket()!!
        val dataUser = viewModel.addDataSeatsDepartureOnly(data, selectedSeatFirst!!)
        if (viewModel.isRoundTrip()) {
            val roundTripData =
                viewModel.addDataSeatsRoundTrip(data, selectedSeatFirst!!, selectedSeatSecond!!)
            CheckoutActivity.startActivity(this, roundTripData)
        } else {
            CheckoutActivity.startActivity(this, dataUser)
        }
    }
}
