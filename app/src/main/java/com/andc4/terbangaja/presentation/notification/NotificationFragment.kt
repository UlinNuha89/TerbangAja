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
import com.andc4.terbangaja.databinding.LayoutBottomsheetNotifBinding
import com.andc4.terbangaja.presentation.login.LoginActivity
import com.andc4.terbangaja.utils.DateUtils.formatIsoDate
import com.andc4.terbangaja.utils.proceedWhen
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
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
        observeViewModel()
        setupRecyclerView()
        viewModel.getNotifications()
    }

    private fun isLogin() {
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
        viewModel.notifications.observe(viewLifecycleOwner) { result ->
            result.proceedWhen(
                doOnSuccess = {
                    updateRecyclerView(it.payload)
                    binding.contentState.root.isVisible = false
                    binding.contentState.pbLoading.isVisible = false
                    binding.contentState.tvError.isVisible = false
                    binding.contentState.ivError.isVisible = false
                    binding.contentState.btnError.isVisible = false
                    binding.rvItemNotification.isVisible = true
                },
                doOnError = {
                },
                doOnLoading = {
                    binding.contentState.pbLoading.isVisible = true
                    binding.contentState.tvError.isVisible = false
                    binding.rvItemNotification.isVisible = false
                },
                doOnEmpty = {
                    binding.contentState.root.isVisible = true
                    binding.contentState.pbLoading.isVisible = false
                    binding.contentState.tvError.isVisible = true
                    binding.contentState.ivError.isVisible = true
                    binding.contentState.ivError.setImageResource(R.drawable.img_empty)
                    binding.contentState.tvError.text = getString(R.string.text_empty_notif)
                    binding.rvItemNotification.isVisible = false
                },
            )
        }

        viewModel.updateResult.observe(viewLifecycleOwner) { result ->
            result.proceedWhen(
                doOnSuccess = {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.notifikasi_telah_dibaca),
                        Toast.LENGTH_SHORT,
                    ).show()
                },
                doOnError = {
                    Log.e("ErrorNotification", it.exception.toString())
                },
            )
        }
    }

    private fun updateRecyclerView(notifications: List<Notification>?) {
        notificationAdapter.clear()
        notifications?.forEach { notification ->
            notificationAdapter.add(
                NotificationItem(notification) { selectedNotification ->
                    showNotifDetail(selectedNotification)
                    viewModel.updateNotification(selectedNotification.id)
                },
            )
        }
    }

    private fun showNotifDetail(selectedNotification: Notification) {
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        val bottomSheetBinding = LayoutBottomsheetNotifBinding.inflate(layoutInflater)
        bottomSheetBinding.apply {
            tvTitleNotification.text = selectedNotification.title
            tvContentNotification.text = selectedNotification.content
            tvDate.text = formatIsoDate(selectedNotification.createdAt)
            tvNotificationCategory.text = selectedNotification.type.replaceFirstChar { it.uppercase() }
        }
        bottomSheetDialog.setContentView(bottomSheetBinding.root)
        bottomSheetDialog.show()
    }
}
