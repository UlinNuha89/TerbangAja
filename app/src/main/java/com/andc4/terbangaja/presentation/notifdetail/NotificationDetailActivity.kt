package com.andc4.terbangaja.presentation.notifdetail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.andc4.terbangaja.data.model.Notification
import com.andc4.terbangaja.databinding.ActivityNotificationDetailBinding

class NotificationDetailActivity : AppCompatActivity() {
    companion object {
        const val EXTRAS_ITEM = "EXTRAS_DETAIL_DATA"

        fun startActivity(
            context: Context,
            notification: Notification,
        ) {
            val intent = Intent(context, NotificationDetailActivity::class.java)
            intent.putExtra(EXTRAS_ITEM, notification)
            context.startActivity(intent)
        }
    }

    private val binding: ActivityNotificationDetailBinding by lazy {
        ActivityNotificationDetailBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindNotificationDetail()
        setContentView(binding.root)
    }

    private fun bindNotificationDetail() {
        intent.extras?.getParcelable<Notification>(EXTRAS_ITEM)?.let {
            binding.tvTitleNotification.text = it.title
            binding.tvContentNotification.text = it.content
            binding.tvDate.text = it.createdAt
        }
    }
}
