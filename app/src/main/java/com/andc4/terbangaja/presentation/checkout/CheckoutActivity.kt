package com.andc4.terbangaja.presentation.checkout

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.andc4.terbangaja.R
import com.andc4.terbangaja.data.model.Passenger
import com.andc4.terbangaja.data.model.UserTicket
import com.andc4.terbangaja.databinding.ActivityCheckoutBinding
import com.andc4.terbangaja.presentation.checkout.adapter.CheckoutAdapter
import com.andc4.terbangaja.presentation.payment.PaymentActivity
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
                    data.returnFlight?.let {
                        val returnPrice = it.economyPrice
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
                    } ?: {
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
                    data.returnFlight?.let {
                        val returnPrice = it.premiumPrice
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
                    } ?: {
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
                    data.returnFlight?.let {
                        val returnPrice = it.businessPrice
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
                    } ?: {
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
                    data.returnFlight?.let {
                        val returnPrice = it.firstClassPrice
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
                    } ?: {
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
            startActivity(Intent(this, PaymentActivity::class.java))
        }
        binding.layoutHeader.ivBackHeader.setOnClickListener {
            onBackPressed()
        }
    }
}
