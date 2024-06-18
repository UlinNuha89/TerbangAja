package com.andc4.terbangaja.presentation.account

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.andc4.terbangaja.R
import com.andc4.terbangaja.databinding.FragmentAccountBinding
import com.andc4.terbangaja.presentation.login.LoginActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class AccountFragment : Fragment() {
    private lateinit var binding: FragmentAccountBinding
    private val viewModel: AccountViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentAccountBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        isLogin()
        setOnClick()
    }

    private fun setOnClick() {
        binding.tvAccountSetting.setOnClickListener {
            navToLogin()
        }
        binding.tvLogout.setOnClickListener {
            viewModel.doLogout()
            isLogin()
        }
    }

    private fun isLogin() {
        if (viewModel.isLogin()) {
            binding.contentState.root.isVisible = false
            binding.contentState.tvError.isVisible = false
            binding.contentState.ivError.isVisible = false
            binding.contentState.btnError.isVisible = false
            hide(true)
        } else {
            binding.contentState.root.isVisible = true
            binding.contentState.tvError.isVisible = true
            binding.contentState.ivError.isVisible = true
            binding.contentState.btnError.isVisible = true
            hide(false)
            binding.contentState.tvError.text = "Maaf, Anda harus login terlebih dahulu"
            binding.contentState.ivError.setImageResource(R.drawable.img_nologin)
            binding.contentState.btnError.text = "Menuju ke Halaman Login"
            binding.contentState.btnError.setOnClickListener {
                navToLogin()
            }
        }
    }

    fun hide(data: Boolean) {
        binding.tvAccountSetting.isVisible = data
        binding.tvLogout.isVisible = data
        binding.tvEditProfile.isVisible = data
        binding.ivAccountSetting.isVisible = data
        binding.ivEdit.isVisible = data
        binding.ivLogout.isVisible = data
        binding.vLineProfile.isVisible = data
        binding.vLineLogout.isVisible = data
        binding.vLineSetting.isVisible = data
    }

    private fun navToLogin() {
        startActivity(Intent(requireContext(), LoginActivity::class.java))
    }
}
