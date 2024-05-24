package com.andc4.terbangaja.presentation.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.andc4.terbangaja.R
import com.andc4.terbangaja.databinding.ActivityMainBinding
import com.andc4.terbangaja.presentation.intro.MyAppIntroActivity

class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupBottomNav()
        checkFirstRun()
    }

    private fun setupBottomNav() {
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        binding.navView.setupWithNavController(navController)
    }

    private fun checkFirstRun() {
        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val isFirstRun = sharedPreferences.getBoolean("isFirstRun", true)
        if (!isFirstRun) {
            sharedPreferences.edit().putBoolean("isFirstRun", false).apply()
            val intent = Intent(this, MyAppIntroActivity::class.java)
            startActivity(intent)
        }
    }
}
