package com.andc4.terbangaja.presentation.notification

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

        viewBinding.root.setOnClickListener {
            clickListener(notification)
        }
    }

    override fun getLayout() = R.layout.item_notification
}
