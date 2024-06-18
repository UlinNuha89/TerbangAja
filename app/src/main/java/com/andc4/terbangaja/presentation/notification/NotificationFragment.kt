package com.andc4.terbangaja.presentation.notification

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.andc4.terbangaja.R
import com.andc4.terbangaja.databinding.FragmentNotificationBinding
import com.andc4.terbangaja.presentation.login.LoginActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class NotificationFragment : Fragment() {
    private lateinit var binding: FragmentNotificationBinding
    private val viewModel: NotificationViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentNotificationBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        isLogin()
    }

    private fun isLogin() {
        if (viewModel.isLogin()) {
            binding.contentState.root.isVisible = false
            binding.contentState.tvError.isVisible = false
            binding.contentState.ivError.isVisible = false
            binding.contentState.btnError.isVisible = false
            binding.rvItemNotification.isVisible = true
        } else {
            binding.contentState.root.isVisible = true
            binding.contentState.tvError.isVisible = true
            binding.contentState.ivError.isVisible = true
            binding.contentState.btnError.isVisible = true
            binding.rvItemNotification.isVisible = false
            binding.contentState.tvError.text = "Maaf, Anda harus login terlebih dahulu"
            binding.contentState.ivError.setImageResource(R.drawable.img_nologin)
            binding.contentState.btnError.text = "Menuju ke Halaman Login"
            binding.contentState.btnError.setOnClickListener {
                navToLogin()
            }
        }
    }

    private fun navToLogin() {
        startActivity(Intent(requireContext(), LoginActivity::class.java))
    }
}
