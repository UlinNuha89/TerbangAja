package com.andc4.terbangaja.presentation.checkout

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.andc4.terbangaja.R
import com.andc4.terbangaja.data.model.Passenger
import com.andc4.terbangaja.data.model.UserTicket
import com.andc4.terbangaja.databinding.ActivityCheckoutBinding
import com.andc4.terbangaja.presentation.checkout.adapter.CheckoutAdapter
import com.andc4.terbangaja.presentation.payment.PaymentActivity
import com.andc4.terbangaja.utils.proceedWhen
import com.andc4.terbangaja.utils.toIndonesianFormat
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class CheckoutActivity : AppCompatActivity() {
    private val binding: ActivityCheckoutBinding by lazy {
        ActivityCheckoutBinding.inflate(layoutInflater)
    }

    private val viewModel: CheckoutViewModel by viewModel {
        parametersOf(intent.extras)
    }
    private val checkoutAdapter: CheckoutAdapter by lazy {
        CheckoutAdapter(viewModel.getUserTicket()!!.seatClass.name) {
        }
    }

    companion object {
        const val EXTRAS_USER_TICKET = "EXTRAS_USER_TICKET"

        fun startActivity(
            context: Context,
            userTicket: UserTicket,
        ) {
            val intent = Intent(context, CheckoutActivity::class.java)
            intent.putExtra(EXTRAS_USER_TICKET, userTicket)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setHeader()
        setUpTicketCheckout()
        bindTicketCheckout()
        setOnClick()
        setBottom()
    }

    private fun setUpTicketCheckout() {
        binding.rvTicketCheckout.apply {
            adapter = checkoutAdapter
        }
    }

    private fun bindTicketCheckout() {
        val data = viewModel.getListFlight()
        checkoutAdapter.submitData(data)
    }

    private fun setBottom() {
        val data = viewModel.getUserTicket()!!
        when (data.seatClass.name) {
            "Economy" -> {
                data.passenger.let { passenger ->
                    bindBottom(passenger)
                    val price = data.departureFlight.economyPrice
                    if (data.returnFlight != null) {
                        val returnPrice = data.returnFlight!!.economyPrice
                        binding.tvAdultsPriceValue.text =
                            getPrice(
                                price, returnPrice, passenger.adult,
                            )
                        binding.tvChildrenPriceValue.text =
                            getPrice(
                                price, returnPrice, passenger.children,
                            )
                        binding.tvBabyPriceValue.text =
                            getPrice(
                                0, 0, 0,
                            )
                        binding.tvPriceTag.text =
                            getPrice(price, returnPrice, getPassenger(passenger))
                    } else {
                        binding.tvAdultsPriceValue.text =
                            getPrice(
                                price, 0, passenger.adult,
                            )
                        binding.tvChildrenPriceValue.text =
                            getPrice(
                                price, 0, passenger.children,
                            )
                        binding.tvBabyPriceValue.text =
                            getPrice(
                                0, 0, 0,
                            )
                        binding.tvPriceTag.text = getPrice(price, 0, getPassenger(passenger))
                    }
                }
            }

            "Premium Economy" -> {
                data.passenger.let { passenger ->
                    bindBottom(passenger)
                    val price = data.departureFlight.premiumPrice
                    if (data.returnFlight != null) {
                        val returnPrice = data.returnFlight!!.premiumPrice
                        binding.tvAdultsPriceValue.text =
                            getPrice(
                                price, returnPrice, passenger.adult,
                            )
                        binding.tvChildrenPriceValue.text =
                            getPrice(
                                price, returnPrice, passenger.children,
                            )
                        binding.tvBabyPriceValue.text =
                            getPrice(
                                0, 0, 0,
                            )
                        binding.tvPriceTag.text =
                            getPrice(price, returnPrice, getPassenger(passenger))
                    } else {
                        binding.tvAdultsPriceValue.text =
                            getPrice(
                                price, 0, passenger.adult,
                            )
                        binding.tvChildrenPriceValue.text =
                            getPrice(
                                price, 0, passenger.children,
                            )
                        binding.tvBabyPriceValue.text =
                            getPrice(
                                0, 0, 0,
                            )
                        binding.tvPriceTag.text = getPrice(price, 0, getPassenger(passenger))
                    }
                }
            }

            "Business" -> {
                data.passenger.let { passenger ->
                    bindBottom(passenger)
                    val price = data.departureFlight.businessPrice
                    if (data.returnFlight != null) {
                        val returnPrice = data.returnFlight!!.businessPrice
                        binding.tvAdultsPriceValue.text =
                            getPrice(
                                price, returnPrice, passenger.adult,
                            )
                        binding.tvChildrenPriceValue.text =
                            getPrice(
                                price, returnPrice, passenger.children,
                            )
                        binding.tvBabyPriceValue.text =
                            getPrice(
                                0, 0, 0,
                            )
                        binding.tvPriceTag.text =
                            getPrice(price, returnPrice, getPassenger(passenger))
                    } else {
                        binding.tvAdultsPriceValue.text =
                            getPrice(
                                price, 0, passenger.adult,
                            )
                        binding.tvChildrenPriceValue.text =
                            getPrice(
                                price, 0, passenger.children,
                            )
                        binding.tvBabyPriceValue.text =
                            getPrice(
                                0, 0, 0,
                            )
                        binding.tvPriceTag.text = getPrice(price, 0, getPassenger(passenger))
                    }
                }
            }

            "First Class" -> {
                data.passenger.let { passenger ->
                    bindBottom(passenger)
                    val price = data.departureFlight.firstClassPrice
                    if (data.returnFlight != null) {
                        val returnPrice = data.returnFlight!!.firstClassPrice
                        binding.tvAdultsPriceValue.text =
                            getPrice(
                                price, returnPrice, passenger.adult,
                            )
                        binding.tvChildrenPriceValue.text =
                            getPrice(
                                price, returnPrice, passenger.children,
                            )
                        binding.tvBabyPriceValue.text =
                            getPrice(
                                0, 0, 0,
                            )
                        binding.tvPriceTag.text =
                            getPrice(price, returnPrice, getPassenger(passenger))
                    } else {
                        binding.tvAdultsPriceValue.text =
                            getPrice(
                                price, 0, passenger.adult,
                            )
                        binding.tvChildrenPriceValue.text =
                            getPrice(
                                price, 0, passenger.children,
                            )
                        binding.tvBabyPriceValue.text =
                            getPrice(
                                0, 0, 0,
                            )
                        binding.tvPriceTag.text = getPrice(price, 0, getPassenger(passenger))
                    }
                }
            }

            else -> {
                binding.tvPriceDetailsTitle.text = getString(R.string.error_occures)
                binding.tvPriceTag.text = getString(R.string.error_occures)
            }
        }
    }

    private fun getPassenger(passenger: Passenger): Int {
        return (passenger.children + passenger.adult)
    }

    private fun getPrice(
        data1: Long,
        data2: Long,
        data3: Int,
    ): String {
        return ((data1 + data2) * data3).toIndonesianFormat() ?: "Rp0,00"
    }

    private fun bindBottom(passenger: Passenger) {
        binding.tvAdultsLabel.text =
            getString(R.string.text_orang_dewasa, passenger.adult.toString())
        binding.tvChildrenLabel.text =
            getString(R.string.text_anak_anak, passenger.children.toString())
        binding.tvBabyLabel.text = getString(R.string.text_bayi, passenger.baby.toString())
    }

    private fun setHeader() {
        binding.layoutHeader.tvTitle.text = getString(R.string.text_title_checkout)
    }

    private fun setOnClick() {
        binding.tvPaymentButton.setOnClickListener {
            doBooking()
        }
        binding.layoutHeader.ivBackHeader.setOnClickListener {
            onBackPressed()
        }
    }

    private fun doBooking() {
        val data = viewModel.getUserTicket()!!
        val price = getTotalPrice(data)
        viewModel.doBooking(data, price).observe(this) {
            it.proceedWhen(
                doOnSuccess = {
                    it.payload?.let {
                        navToPayment(it)
                    }
                },
                doOnError = {
                    Toast.makeText(this, "${it.exception?.cause?.message}", Toast.LENGTH_SHORT)
                        .show()
                },
            )
        }
    }

    private fun navToPayment(link: String) {
        PaymentActivity.startActivity(this, link)
    }

    private fun getTotalPrice(userTicket: UserTicket): Int {
        return when (userTicket.seatClass.name) {
            "Economy" -> {
                if (userTicket.returnFlight == null) {
                    getPassenger(userTicket.passenger) * (userTicket.departureFlight.economyPrice.toInt())
                } else {
                    getPassenger(userTicket.passenger) * (userTicket.returnFlight!!.economyPrice.toInt() + userTicket.returnFlight!!.economyPrice.toInt())
                }
            }

            "Premium Economy" -> {
                if (userTicket.returnFlight == null) {
                    getPassenger(userTicket.passenger) * (userTicket.departureFlight.premiumPrice.toInt())
                } else {
                    getPassenger(userTicket.passenger) * (userTicket.returnFlight!!.premiumPrice.toInt() + userTicket.returnFlight!!.premiumPrice.toInt())
                }
            }

            "Business" -> {
                if (userTicket.returnFlight == null) {
                    getPassenger(userTicket.passenger) * (userTicket.departureFlight.businessPrice.toInt())
                } else {
                    getPassenger(userTicket.passenger) * (userTicket.returnFlight!!.businessPrice.toInt() + userTicket.returnFlight!!.businessPrice.toInt())
                }
            }

            "First Class" -> {
                if (userTicket.returnFlight == null) {
                    getPassenger(userTicket.passenger) * (userTicket.departureFlight.firstClassPrice.toInt())
                } else {
                    getPassenger(userTicket.passenger) * (userTicket.returnFlight!!.firstClassPrice.toInt() + userTicket.returnFlight!!.firstClassPrice.toInt())
                }
            }

            else -> 0
        }
    }
}
