package com.andc4.terbangaja.presentation.common

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.andc4.terbangaja.R
import com.andc4.terbangaja.databinding.FragmentCommonBinding
import com.andc4.terbangaja.presentation.login.LoginActivity
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class CommonFragment() : BottomSheetDialogFragment() {
    private var _binding: FragmentCommonBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentCommonBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        getData()
        setOnClick()
    }

    private fun setOnClick() {
        binding.ivCross.setOnClickListener {
            dismiss()
        }
    }

    private fun getData() {
        val data = arguments?.getBoolean("isAvailable")
        if (data!!) {
            val isLogin = arguments?.getBoolean("isLogin")
            if (!isLogin!!) {
                showNoLogin()
            }
        } else {
            showNoTicket()
        }
    }

    private fun showNoLogin() {
        binding.contentStateLogin.root.isVisible = true
        binding.contentStateLogin.ivError.isVisible = true
        binding.contentStateLogin.tvError.isVisible = true
        binding.contentStateLogin.btnError.isVisible = true
        binding.contentStateLogin.pbLoading.isVisible = false
        binding.contentStateTicket.root.isVisible = false
        binding.contentStateTicket.ivEmptyTicket.isVisible = false
        binding.contentStateTicket.tvEmptyTicket.isVisible = false
        binding.contentStateTicket.tvEmptyTicketSub.isVisible = false
        binding.contentStateLogin.ivError.setImageResource(R.drawable.img_nologin)
        binding.contentStateLogin.tvError.text = getString(R.string.text_no_login)
        binding.contentStateLogin.btnError.text = getString(R.string.text_btn_no_login)
        binding.contentStateLogin.btnError.setOnClickListener {
            navigateToLogin()
        }
    }

    private fun showNoTicket() {
        binding.contentStateTicket.root.isVisible = true
        binding.contentStateTicket.ivEmptyTicket.isVisible = true
        binding.contentStateTicket.tvEmptyTicket.isVisible = true
        binding.contentStateTicket.tvEmptyTicketSub.isVisible = true
        binding.contentStateLogin.root.isVisible = false
        binding.contentStateLogin.ivError.isVisible = false
        binding.contentStateLogin.tvError.isVisible = false
        binding.contentStateLogin.btnError.isVisible = false
        binding.contentStateLogin.pbLoading.isVisible = false
        binding.contentStateTicket.ivEmptyTicket.setImageResource(R.drawable.img_empty)
        binding.contentStateTicket.tvEmptyTicket.text = getString(R.string.text_empty_top)
        binding.contentStateTicket.tvEmptyTicketSub.text = getString(R.string.text_empty_bottom)
    }

    private fun navigateToLogin() {
        startActivity(
            Intent(requireContext(), LoginActivity::class.java),
        )
    }
}
