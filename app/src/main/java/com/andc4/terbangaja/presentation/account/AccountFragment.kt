package com.andc4.terbangaja.presentation.account

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.andc4.terbangaja.R
import com.andc4.terbangaja.data.model.User
import com.andc4.terbangaja.databinding.FragmentAccountBinding
import com.andc4.terbangaja.presentation.account.edit.EditProfileActivity
import com.andc4.terbangaja.presentation.login.LoginActivity
import com.andc4.terbangaja.presentation.resetpassword.ResetPasswordActivity
import com.andc4.terbangaja.utils.proceedWhen
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
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
        // isLogin()
        setOnClick()
        getProfile()
    }

    private fun getProfile() {
        viewModel.getProfile().observe(viewLifecycleOwner) { result ->
            result.proceedWhen(
                doOnLoading = {
                    binding.contentState.pbLoading.isVisible = true
                    binding.contentState.tvError.isVisible = false
                    hide(false)
                },
                doOnSuccess = {
                    binding.contentState.pbLoading.isVisible = false
                    binding.contentState.tvError.isVisible = false
                    hide(true)
                    it.payload?.let { data ->
                        bindProfileData(data)
                    }
                },
                doOnError = {
                    binding.contentState.pbLoading.isVisible = false
                    binding.contentState.root.isVisible = true
                    binding.contentState.tvError.isVisible = true
                    binding.contentState.ivError.isVisible = true
                    binding.contentState.btnError.isVisible = true
                    hide(false)
                    when (it.exception?.cause?.message.toString()) {
                        "401" -> {
                            binding.contentState.tvError.text =
                                "Maaf, Anda harus login terlebih dahulu"
                            binding.contentState.ivError.setImageResource(R.drawable.img_nologin)
                            binding.contentState.btnError.text = "Menuju ke Halaman Login"
                            binding.contentState.btnError.setOnClickListener {
                                navToLogin()
                            }
                        }
                    }
                },
                doOnEmpty = {
                    binding.contentState.root.isVisible = true
                    binding.contentState.pbLoading.isVisible = false
                    binding.contentState.tvError.isVisible = true
                    binding.contentState.tvError.text = it.message
                    hide(false)
                },
            )
        }
    }

    private fun bindProfileData(profile: User) {
        binding.tvName.text = profile.name
        binding.tvEmail.text = profile.email
        binding.tvNoHp.text = profile.phone
        Glide.with(requireContext())
            .load(profile.photo)
            .apply(RequestOptions.circleCropTransform())
            .into(binding.ivProfile)
    }

    private fun setOnClick() {
        binding.cvChangePassword.setOnClickListener {
            navResetPassword()
        }
        binding.cvEditProfile.setOnClickListener {
            navEditProfile()
        }
        binding.tvLogout.setOnClickListener {
            showLogoutConfirmationDialog()
        }
    }

    fun hide(data: Boolean) {
        binding.tvChangePassword.isVisible = data
        binding.tvLogout.isVisible = data
        binding.tvEditProfile.isVisible = data
        binding.ivAccountSetting.isVisible = data
        binding.ivEdit.isVisible = data
        binding.ivLogout.isVisible = data
        binding.cvUbahProfil.isVisible = data
        binding.cvChangePassword.isVisible = data
        binding.cvLogout.isVisible = data
        binding.ivProfile.isVisible = data
        binding.tvName.isVisible = data
        binding.tvEmail.isVisible = data
        binding.tvNoHp.isVisible = data
        binding.cvAkun.isVisible = data
        binding.ivBgAkunProfile.isVisible = data
    }

    private fun navResetPassword() {
        startActivity(Intent(requireContext(), ResetPasswordActivity::class.java))
    }

    private fun navEditProfile() {
        startActivity(Intent(requireContext(), EditProfileActivity::class.java))
    }

    private fun navToLogin() {
        startActivity(Intent(requireContext(), LoginActivity::class.java))
    }

    private fun showLogoutConfirmationDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setMessage("Apakah Anda yakin ingin keluar dari akun?")
            .setPositiveButton("Iya") { dialog, id ->
                viewModel.doLogout()
                getProfile()
                dialog.dismiss()
            }
            .setNegativeButton("Tidak") { dialog, id ->
                dialog.dismiss()
            }
        val alert = builder.create()
        alert.show()
    }
}
