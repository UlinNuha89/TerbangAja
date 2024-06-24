package com.andc4.terbangaja.presentation.multiform

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.andc4.terbangaja.R
import com.andc4.terbangaja.data.model.Ticket
import com.andc4.terbangaja.databinding.ItemPassengerMultiFormBinding
import com.andc4.terbangaja.presentation.ticketorder.TicketOrderActivity

class ActivityMultiPassengerBio : AppCompatActivity() {

    private val binding: ItemPassengerMultiFormBinding by lazy {
        ItemPassengerMultiFormBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val title = resources.getStringArray(R.array.title)
        val arrayAdapterTitle = ArrayAdapter(this, R.layout.item_dropdown, title)
        binding.acTitle.setAdapter(arrayAdapterTitle)

        val nation = resources.getStringArray(R.array.nation)
        val arrayAdapterNation = ArrayAdapter(this, R.layout.item_dropdown, nation)
        binding.acNationPublisher.setAdapter(arrayAdapterNation)

    }

    private fun sendData() {
        val btnSave = findViewById<Button>(R.id.btn_save)
        val etTitle = binding.acTitle
        val etName = binding.etName
        val etFamilyName = binding.etFamilyName
        val etBirthDate = binding.etBirthDate
        val etNationality = binding.etNationality
        val etPassport = binding.etPassport
        val etNationPublisher = binding.acNationPublisher
        val etExpire = binding.etExpire

        btnSave.setOnClickListener {
            val title = etTitle.text.toString()
            val name = etName.text.toString()
            val familyName = etFamilyName.text.toString()
            val birthDate = etBirthDate.text.toString().toInt()
            val nationality = etNationality.text.toString()
            val passport = etPassport.text.toString().toInt()
            val nationPublisher = etNationPublisher.text.toString()
            val expire = etExpire.text.toString().toInt()
            intent(this)
        }
    }

    companion object {
        const val EXTRAS_FORM = "EXTRAS_FORM"

        fun startActivity(
            context: Context,
        ) {
            val intent = Intent(context, TicketOrderActivity::class.java)
            intent.putExtra(EXTRAS_FORM)
            context.startActivity(intent)
        }
    }


}