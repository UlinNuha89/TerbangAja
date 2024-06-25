package com.andc4.terbangaja.presentation.datapassenger.adapter

import android.app.DatePickerDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.andc4.terbangaja.data.model.FormPassenger
import com.andc4.terbangaja.data.model.UserTicket
import com.andc4.terbangaja.databinding.ItemFormPassengerBinding
import com.google.android.material.textfield.TextInputEditText
import java.util.Calendar

class PassengerAdapter(
    private val userTicket: UserTicket,
) : RecyclerView.Adapter<PassengerAdapter.PassengerViewHolder>() {
    private var numAdult = 1
    private val numForms = userTicket.passenger.adult + userTicket.passenger.children
    private val formDataList =
        MutableList(numForms) { FormPassenger("", "", "", "", "", "", "", "") }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): PassengerViewHolder {
        val binding =
            ItemFormPassengerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PassengerViewHolder(userTicket, binding)
    }

    override fun getItemCount(): Int = numForms

    override fun onBindViewHolder(
        holder: PassengerViewHolder,
        position: Int,
    ) {
        holder.bind(formDataList[position])
    }

    fun getFormData(): List<FormPassenger> = formDataList

    inner class PassengerViewHolder(
        private val userTicket: UserTicket,
        val binding: ItemFormPassengerBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(formPassenger: FormPassenger) {
            getTitleCard(userTicket)
            // Title dropdown setup
            val titles = arrayOf("Mr.", "Mrs.", "Ms.")
            val titleAdapter =
                ArrayAdapter(
                    binding.root.context,
                    android.R.layout.simple_spinner_dropdown_item,
                    titles,
                )
            titleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerTitle.adapter = titleAdapter
            binding.spinnerTitle.setSelection(titles.indexOf(formPassenger.title))
            binding.spinnerTitle.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>,
                        view: View,
                        position: Int,
                        id: Long,
                    ) {
                        formPassenger.title = titles[position]
                    }

                    override fun onNothingSelected(parent: AdapterView<*>) {}
                }

            // Full name setup
            binding.fullName.setText(formPassenger.fullName)
            binding.fullName.doOnTextChanged { text, _, _, _ ->
                formPassenger.fullName = text.toString()
            }

            // Family name setup
            binding.familyNameSwitch.setOnCheckedChangeListener { _, isChecked ->
                binding.familyNameLayout.visibility = if (isChecked) View.VISIBLE else View.GONE
            }
            binding.familyName.setText(formPassenger.familyName)
            binding.familyName.doOnTextChanged { text, _, _, _ ->
                formPassenger.familyName = text.toString()
            }

            // Birth date setup
            binding.birthDate.setText(formPassenger.birthDate)
            binding.birthDate.setOnClickListener { showDatePickerDialog(it as TextInputEditText) }
            binding.birthDate.doOnTextChanged { text, _, _, _ ->
                formPassenger.birthDate = text.toString()
            }

            // Citizenship setup
            binding.citizenship.setText(formPassenger.citizenship)
            binding.citizenship.doOnTextChanged { text, _, _, _ ->
                formPassenger.citizenship = text.toString()
            }

            // ID passport setup
            binding.idPassport.setText(formPassenger.identityNumber)
            binding.idPassport.doOnTextChanged { text, _, _, _ ->
                formPassenger.identityNumber = text.toString()
            }

            // Publisher country dropdown setup
            val countries =
                arrayOf("Indonesia", "Amerika Serikat", "China", "Jepang", "Thailand")
            val countryAdapter =
                ArrayAdapter(
                    binding.root.context,
                    android.R.layout.simple_spinner_dropdown_item,
                    countries,
                )
            countryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerPublisherCountry.adapter = countryAdapter
            binding.spinnerPublisherCountry.setSelection(countries.indexOf(formPassenger.publisherCountry))
            binding.spinnerPublisherCountry.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>,
                        view: View,
                        position: Int,
                        id: Long,
                    ) {
                        formPassenger.publisherCountry = countries[position]
                    }

                    override fun onNothingSelected(parent: AdapterView<*>) {}
                }

            // Expired date setup
            binding.expiredDate.setText(formPassenger.identityExpireDate)
            binding.expiredDate.setOnClickListener { showDatePickerDialog(it as TextInputEditText) }
            binding.expiredDate.doOnTextChanged { text, _, _, _ ->
                formPassenger.identityExpireDate = text.toString()
            }
        }

        private fun getTitleCard(userTicket: UserTicket) {
            if (numAdult <= userTicket.passenger.adult) {
                binding.tvPassengerBio.text =
                    "Penumpang ${layoutPosition + 1} - Dewasa"
                numAdult++
            } else {
                binding.tvPassengerBio.text =
                    "Penumpang ${layoutPosition + 1} - Anak-anak"
            }
        }
    }

    private fun showDatePickerDialog(editText: TextInputEditText) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog =
            DatePickerDialog(
                editText.context,
                { _, selectedYear, selectedMonth, selectedDay ->
                    val date = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                    editText.setText(date)
                },
                year,
                month,
                day,
            )
        datePickerDialog.show()
    }
}
