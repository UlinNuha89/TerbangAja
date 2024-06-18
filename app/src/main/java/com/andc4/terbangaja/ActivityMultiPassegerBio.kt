package com.andc4.terbangaja

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.andc4.terbangaja.databinding.ActivityMultiPassengerBioBinding
import com.andc4.terbangaja.databinding.ActivitySeatBinding
import com.andc4.terbangaja.databinding.FragmentHomeBinding
import com.andc4.terbangaja.databinding.ItemPassengerMultiFormBinding

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
}
