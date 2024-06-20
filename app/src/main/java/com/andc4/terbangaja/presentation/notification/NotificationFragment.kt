package com.andc4.terbangaja.presentation.notification

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.andc4.terbangaja.R
import com.andc4.terbangaja.data.model.Notification
import com.andc4.terbangaja.databinding.FragmentNotificationBinding
import com.andc4.terbangaja.presentation.login.LoginActivity
import com.andc4.terbangaja.presentation.notifdetail.NotificationDetailActivity
import com.andc4.terbangaja.utils.proceedWhen
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.andc4.terbangaja.utils.proceedWhen
import org.koin.androidx.viewmodel.ext.android.viewModel

class NotificationFragment : Fragment() {
    private lateinit var binding: FragmentNotificationBinding
    private val notificationAdapter = GroupAdapter<GroupieViewHolder>()
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
        setupRecyclerView()
    }

    private fun isLogin() {
        if (viewModel.isLogin()) {
            binding.contentState.root.isVisible = false
            binding.contentState.tvError.isVisible = false
            binding.contentState.ivError.isVisible = false
            binding.contentState.btnError.isVisible = false
            binding.rvItemNotification.isVisible = true
            observeViewModel()
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
        viewModel.isLogin().observe(viewLifecycleOwner) {
            it.proceedWhen(
                doOnLoading = {
                    binding.contentState.pbLoading.isVisible = true
                    binding.contentState.tvError.isVisible = false
                },
                doOnSuccess = {
                    binding.contentState.root.isVisible = false
                    binding.contentState.pbLoading.isVisible = false
                    binding.contentState.tvError.isVisible = false
                    binding.contentState.ivError.isVisible = false
                    binding.contentState.btnError.isVisible = false
                    binding.rvItemNotification.isVisible = true
                },
                doOnError = {
                    binding.contentState.root.isVisible = true
                    binding.contentState.tvError.isVisible = true
                    binding.contentState.pbLoading.isVisible = false
                    binding.contentState.ivError.isVisible = true
                    binding.rvItemNotification.isVisible = false
                    binding.contentState.tvError.text = it.exception?.cause?.message
                    when (it.exception?.cause?.message.toString()) {
                        "401" -> {
                            binding.contentState.btnError.isVisible = true
                            binding.contentState.tvError.text = getString(R.string.text_no_login)
                            binding.contentState.ivError.setImageResource(R.drawable.img_nologin)
                            binding.contentState.btnError.text =
                                getString(R.string.text_btn_no_login)
                            binding.contentState.btnError.setOnClickListener {
                                navToLogin()
                            }
                        }
                    }
                },
            )
        }
    }

    private fun navToLogin() {
        startActivity(Intent(requireContext(), LoginActivity::class.java))
    }

    private fun setupRecyclerView() {
        binding.rvItemNotification.apply {
            adapter = notificationAdapter
        }
    }

    private fun observeViewModel() {
        viewModel.getNotifications().observe(viewLifecycleOwner) { it ->
            it.proceedWhen(
                doOnSuccess = {
                    updateRecyclerView(it.payload)
                    Toast.makeText(requireContext(), "Sukses", Toast.LENGTH_SHORT).show()
                },
                doOnError = {
                    Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
                    Log.e("ErrorNotif", it.exception.toString())
                },
                doOnLoading = {
                    Toast.makeText(requireContext(), "loading", Toast.LENGTH_SHORT).show()
                },
                doOnEmpty = {
                    Toast.makeText(requireContext(), "Kosong", Toast.LENGTH_SHORT).show()
                },
            )
        }
    }

    private fun updateRecyclerView(notifications: List<Notification>?) {
        notificationAdapter.clear()
        notifications?.forEach { notification ->
            notificationAdapter.add(
                NotificationItem(notification) { selectedNotification ->
                    NotificationDetailActivity.startActivity(requireContext(), selectedNotification)
                },
            )
        }
    }
}
