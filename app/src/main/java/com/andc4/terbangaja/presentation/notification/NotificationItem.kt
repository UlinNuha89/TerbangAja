package com.andc4.terbangaja.presentation.notification

import android.graphics.Typeface
import com.andc4.terbangaja.R
import com.andc4.terbangaja.data.model.Notification
import com.andc4.terbangaja.databinding.ItemNotificationBinding
import com.andc4.terbangaja.utils.DateUtils
import com.xwray.groupie.databinding.BindableItem

class NotificationItem(
    private val notification: Notification,
    private val clickListener: (Notification) -> Unit,
) : BindableItem<ItemNotificationBinding>() {
    override fun bind(
        viewBinding: ItemNotificationBinding,
        position: Int,
    ) {
        viewBinding.tvNotificationCategory.text = notification.type.replaceFirstChar { it.uppercase() }
        viewBinding.tvDate.text = DateUtils.timeAgo(notification.createdAt)
        viewBinding.tvNotification.text = notification.title

        // Function to update UI based on read status
        fun updateReadStatus(isRead: Boolean) {
            if (!isRead) {
                viewBinding.root.setBackgroundResource(R.drawable.bg_rounded_shape_purple)
                viewBinding.tvNotificationCategory.setTypeface(null, Typeface.BOLD)
                viewBinding.tvNotification.setTypeface(null, Typeface.BOLD)
                viewBinding.tvDate.setTypeface(null, Typeface.BOLD)
            } else {
                viewBinding.root.setBackgroundResource(R.drawable.bg_rounded_shape_white)
                viewBinding.tvNotificationCategory.setTypeface(null, Typeface.NORMAL)
                viewBinding.tvNotification.setTypeface(null, Typeface.NORMAL)
                viewBinding.tvDate.setTypeface(null, Typeface.NORMAL)
            }
        }

        updateReadStatus(notification.isRead)

        viewBinding.root.setOnClickListener {
            clickListener(notification)
            notification.isRead = true
            updateReadStatus(notification.isRead)
        }
    }

    override fun getLayout() = R.layout.item_notification
}
