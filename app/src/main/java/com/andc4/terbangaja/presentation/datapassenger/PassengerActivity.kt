package com.andc4.terbangaja.presentation.datapassenger

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isEmpty
import com.andc4.terbangaja.R
import com.andc4.terbangaja.data.model.FormPassenger
import com.andc4.terbangaja.data.model.UserTicket
import com.andc4.terbangaja.databinding.ActivityPassengerBinding
import com.andc4.terbangaja.presentation.datapassenger.adapter.PassengerAdapter
import com.andc4.terbangaja.presentation.seat.SeatActivity
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PassengerActivity : AppCompatActivity() {
    private val binding: ActivityPassengerBinding by lazy {
        ActivityPassengerBinding.inflate(layoutInflater)
    }
    private val viewModel: PassengerViewModel by viewModel {
        parametersOf(intent.extras)
    }

    private val passengerAdapter: PassengerAdapter by lazy {
        PassengerAdapter(viewModel.getDataUserTicket()!!)
    }

    companion object {
        const val EXTRAS_USER_TICKET = "EXTRAS_USER_TICKET"
        const val EXTRAS_IS_ROUND_TRIP = "EXTRAS_IS_ROUND_TRIP"

        fun startActivity(
            context: Context,
            userTicket: UserTicket,
            isRoundTrip: Boolean,
        ) {
            val intent = Intent(context, PassengerActivity::class.java)
            intent.putExtra(EXTRAS_USER_TICKET, userTicket)
            intent.putExtra(EXTRAS_IS_ROUND_TRIP, isRoundTrip)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupHeader()
        setUpAdapter()
        setOnClick()
    }

    private fun setupHeader() {
        binding.layoutHeader.tvTitle.text = getString(R.string.title_biodata_penumpang)
    }

    private fun setUpAdapter() {
        binding.rvForm.apply {
            adapter = passengerAdapter
        }
    }

    private fun setOnClick() {
        binding.layoutHeader.ivBackHeader.setOnClickListener {
            onBackPressed()
        }
        binding.btnConfirm.setOnClickListener {
            val data = passengerAdapter.getFormData()
            if (isFormValid()) {
                navToSeat(data)
            } else {
                Toast.makeText(
                    this,
                    getString(R.string.mohon_isi_data_dengan_lengkap),
                    Toast.LENGTH_SHORT,
                ).show()
            }
        }
    }

    private fun navToSeat(dataPassenger: List<FormPassenger>) {
        val temp = viewModel.getDataUserTicket()!!
        val data = viewModel.addDataPassenger(temp, dataPassenger)
        SeatActivity.startActivity(this, data, viewModel.getIsRoundTrip())
    }

    private fun isFormValid(): Boolean {
        for (i in 0 until binding.rvForm.childCount) {
            val viewHolder =
                binding.rvForm.findViewHolderForAdapterPosition(i) as PassengerAdapter.PassengerViewHolder
            with(viewHolder.binding) {
                if (spinnerTitle.isEmpty() || fullName.text.isNullOrEmpty() || birthDate.text.isNullOrEmpty() ||
                    citizenship.text.isNullOrEmpty() || idPassport.text.isNullOrEmpty() || spinnerPublisherCountry.isEmpty() ||
                    expiredDate.text.isNullOrEmpty() || (familyNameSwitch.isChecked && familyName.text.isNullOrEmpty())
                ) {
                    return false
                }
            }
        }
        return true
    }
}
