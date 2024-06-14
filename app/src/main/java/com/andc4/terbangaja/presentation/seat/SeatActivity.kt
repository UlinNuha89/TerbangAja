package com.andc4.terbangaja.presentation.seat

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.andc4.terbangaja.R
import com.andc4.terbangaja.databinding.ActivitySeatBinding
import com.andc4.terbangaja.presentation.checkout.CheckoutActivity
import dev.jahidhasanco.seatbookview.SeatBookView
import dev.jahidhasanco.seatbookview.SeatClickListener
import dev.jahidhasanco.seatbookview.SeatLongClickListener

class SeatActivity : AppCompatActivity() {
    private lateinit var seatBookView: SeatBookView
    private val binding: ActivitySeatBinding by lazy {
        ActivitySeatBinding.inflate(layoutInflater)
    }
    private var seatCounter = 1

    private var seats = (
        "/UUURUUU" +
            "/AAARUUU" +
            "/UUURAAA" +
            "/UUARAAA" +
            "/UUURAAA" +
            "/AAARAAU" +
            "/AAARAAA" +
            "/UUURAAA" +
            "/UUURAAA" +
            "/UUURAAA" +
            "/UUURUUU" +
            "/AAARUUU" +
            "/UUURAAA"
    )

    // buat ada nomor tempat duduk!!!
    private var title =
        listOf(
            "/", "  ", "  ", "  ", "1", "  ", "  ", "  ",
            "/", "  ", "  ", "  ", "2", "  ", "  ", "  ",
            "/", "  ", "  ", "  ", "3", "  ", "  ", "  ",
            "/", "  ", "  ", "  ", "4", "  ", "  ", "  ",
            "/", "  ", "  ", "  ", "5", "  ", "  ", "  ",
            "/", "  ", "  ", "  ", "6", "  ", "  ", "  ",
            "/", "  ", "  ", "  ", "7", "  ", "  ", "  ",
            "/", "  ", "  ", "  ", "8", "  ", "  ", "  ",
            "/", "  ", "  ", "  ", "9", "  ", "  ", "  ",
            "/", "  ", "  ", "  ", "10", "  ", "  ", "  ",
            "/", "  ", "  ", "  ", "11", "  ", "  ", "  ",
            "/", "  ", "  ", "  ", "12", "  ", "  ", "  ",
            "/", "  ", "  ", "  ", "13", "  ", "  ", "  ",
        )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setOnClick()
        seatBookView = findViewById(R.id.layoutSeat)
        seatBookView.setSeatsLayoutString(seats)
            .isCustomTitle(true)
            .setCustomTitle(title)
            .setSeatLayoutPadding(2)
            .setSelectSeatLimit(6)
            .setSeatSizeBySeatsColumnAndLayoutWidth(7, -1)
        seatBookView.show()

        seatBookView.setSeatClickListener(
            object : SeatClickListener {
                override fun onAvailableSeatClick(
                    selectedIdList: List<Int>,
                    view: View,
                ) {
                    /*
                    ini gak perlu dibuat kayaknya
                    tinggal export data dari seat-nya aja ke variabel data pake listof

                    view.setBackgroundResource(R.drawable.bg_selected)
                    (view as TextView).text = "P$seatCounter"
                    view.setTextColor(resources.getColor(android.R.color.white))
                    seatCounter++
                    if (seatBookView.isActivated) {
                        view.setBackgroundResource(R.drawable.bg_booked)
                        (view as TextView).text = "  "
                        view.setTextColor(resources.getColor(android.R.color.white))
                        seatCounter++
                    }*/
                }

                override fun onBookedSeatClick(view: View) {
                    Toast.makeText(this@SeatActivity, "Seat is Booked", Toast.LENGTH_SHORT).show()
                }

                override fun onReservedSeatClick(view: View) {
                    // Handle reserved seat click
                }
            },
        )

        seatBookView.setSeatLongClickListener(
            object : SeatLongClickListener {
                override fun onAvailableSeatLongClick(view: View) {
                    Toast.makeText(this@SeatActivity, "Seat Succes", Toast.LENGTH_SHORT).show()
                }

                override fun onBookedSeatLongClick(view: View) {
                    Toast.makeText(this@SeatActivity, "Seat is Booked", Toast.LENGTH_SHORT).show()
                }

                override fun onReservedSeatLongClick(view: View) {
                    // Handle reserved seat long click
                }
            },
        )
    }

    private fun setOnClick() {
        binding.btnSave.setOnClickListener {
            doSomething()
        }
    }

    private fun doSomething() {
        startActivity(Intent(this, CheckoutActivity::class.java))
    }
}
