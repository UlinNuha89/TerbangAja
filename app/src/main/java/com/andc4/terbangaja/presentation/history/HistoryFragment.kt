package com.andc4.terbangaja.presentation.history

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.andc4.terbangaja.R
import com.andc4.terbangaja.databinding.FragmentHistoryBinding
import com.andc4.terbangaja.presentation.datapassenger.PassengerActivity
import com.andc4.terbangaja.presentation.login.LoginActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class HistoryFragment : Fragment() {
    private lateinit var binding: FragmentHistoryBinding
    private val viewModel: HistoryViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentHistoryBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        isLogin()
        navToForm()
    }

    private fun navToForm() {
        startActivity(Intent(requireContext(), PassengerActivity::class.java))
    }

    private fun isLogin() {
        if (viewModel.isLogin()) {
            binding.contentState.root.isVisible = false
            binding.contentState.tvError.isVisible = false
            binding.contentState.ivError.isVisible = false
            binding.contentState.btnError.isVisible = false
            binding.rvHistoryList.isVisible = true
        } else {
            binding.contentState.root.isVisible = true
            binding.contentState.tvError.isVisible = true
            binding.contentState.ivError.isVisible = true
            binding.contentState.btnError.isVisible = true
            binding.rvHistoryList.isVisible = false
            binding.contentState.tvError.text = getString(R.string.text_no_login)
            binding.contentState.ivError.setImageResource(R.drawable.img_nologin)
            binding.contentState.btnError.text = getString(R.string.text_btn_no_login)
            binding.contentState.btnError.setOnClickListener {
                navToLogin()
            }
        }
    }

    private fun navToLogin() {
        startActivity(Intent(requireContext(), LoginActivity::class.java))
    }
}
