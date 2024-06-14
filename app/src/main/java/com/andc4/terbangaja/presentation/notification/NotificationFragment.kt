package com.andc4.terbangaja.presentation.notification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.andc4.terbangaja.databinding.FragmentNotificationBinding
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
    }

    private fun isLogin() {
        viewModel.isLogin()
    }
}
