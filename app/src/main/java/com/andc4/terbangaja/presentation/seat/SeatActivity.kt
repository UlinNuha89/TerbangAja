package com.andc4.terbangaja.presentation.seat

import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.setPadding
import com.andc4.terbangaja.R
import dev.jahidhasanco.seatbookview.SeatBookView
import dev.jahidhasanco.seatbookview.SeatClickListener
import dev.jahidhasanco.seatbookview.SeatLongClickListener

class SeatActivity : AppCompatActivity() {
    private lateinit var seatBookView: SeatBookView
    private var seatCounter = 1

    private var seats = (
        "/UUUSUUU" +
            "/AAASUUU" +
            "/UUUSAAA" +
            "/UUASAAA" +
            "/UUUSAAA" +
            "/AAASAAU" +
            "/AAASAAA" +
            "/UUUSAAA" +
            "/UUUSAAA" +
            "/UUUSAAA" +
            "/UUUSUUU" +
            "/AAASUUU" +
            "/UUUSAAA"
    )

    private var title =
        listOf(
            "/", "  ", "  ", "  ", "  ", "  ", "  ", "  ",
            "/", "  ", "  ", "  ", "  ", "  ", "  ", "  ",
            "/", "  ", "  ", "  ", "  ", "  ", "  ", "  ",
            "/", "  ", "  ", "  ", "  ", "  ", "  ", "  ",
            "/", "  ", "  ", "  ", "  ", "  ", "  ", "  ",
            "/", "  ", "  ", "  ", "  ", "  ", "  ", "  ",
            "/", "  ", "  ", "  ", "  ", "  ", "  ", "  ",
            "/", "  ", "  ", "  ", "  ", "  ", "  ", "  ",
            "/", "  ", "  ", "  ", "  ", "  ", "  ", "  ",
            "/", "  ", "  ", "  ", "  ", "  ", "  ", "  ",
            "/", "  ", "  ", "  ", "  ", "  ", "  ", "  ",
            "/", "  ", "  ", "  ", "  ", "  ", "  ", "  ",
            "/", "  ", "  ", "  ", "  ", "  ", "  ", "  ",
        )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seat)

        seatBookView = findViewById(R.id.layoutSeat)
        seatBookView.setSeatsLayoutString(seats)
            .isCustomTitle(true)
            .setCustomTitle(title)
            .setSeatLayoutPadding(2)
            .setSeatSizeBySeatsColumnAndLayoutWidth(7, -1)
        seatBookView.show()

        seatBookView.getSeatView(4).apply {
            seatBookView.markAsTransparentSeat(this as TextView)
            this.setBackgroundResource(R.drawable.bg_chair_spacing)
            this.setPadding(5)
            this.setText("1")
        }
        seatBookView.getSeatView(11).apply {
            seatBookView.markAsTransparentSeat(this as TextView)
            this.setBackgroundResource(R.drawable.bg_chair_spacing)
            this.setPadding(5)
            this.setText("2")
        }
        seatBookView.getSeatView(18).apply {
            seatBookView.markAsTransparentSeat(this as TextView)
            this.setBackgroundResource(R.drawable.bg_chair_spacing)
            this.setPadding(5)
            this.setText("3")
        }
        seatBookView.getSeatView(25).apply {
            seatBookView.markAsTransparentSeat(this as TextView)
            this.setBackgroundResource(R.drawable.bg_chair_spacing)
            this.setPadding(5)
            this.setText("4")
        }
        seatBookView.getSeatView(32).apply {
            seatBookView.markAsTransparentSeat(this as TextView)
            this.setBackgroundResource(R.drawable.bg_chair_spacing)
            this.setPadding(5)
            this.setText("5")
        }
        seatBookView.getSeatView(39).apply {
            seatBookView.markAsTransparentSeat(this as TextView)
            this.setBackgroundResource(R.drawable.bg_chair_spacing)
            this.setPadding(5)
            this.setText("6")
        }
        seatBookView.getSeatView(46).apply {
            seatBookView.markAsTransparentSeat(this as TextView)
            this.setBackgroundResource(R.drawable.bg_chair_spacing)
            this.setPadding(5)
            this.setText("7")
        }
        seatBookView.getSeatView(53).apply {
            seatBookView.markAsTransparentSeat(this as TextView)
            this.setBackgroundResource(R.drawable.bg_chair_spacing)
            this.setPadding(5)
            this.setText("8")
        }
        seatBookView.getSeatView(60).apply {
            seatBookView.markAsTransparentSeat(this as TextView)
            this.setBackgroundResource(R.drawable.bg_chair_spacing)
            this.setPadding(5)
            this.setText("9")
        }
        seatBookView.getSeatView(67).apply {
            seatBookView.markAsTransparentSeat(this as TextView)
            this.setBackgroundResource(R.drawable.bg_chair_spacing)
            this.setPadding(5)
            this.setText("10")
        }
        seatBookView.getSeatView(74).apply {
            seatBookView.markAsTransparentSeat(this as TextView)
            this.setBackgroundResource(R.drawable.bg_chair_spacing)
            this.setPadding(5)
            this.setText("11")
        }
        seatBookView.getSeatView(81).apply {
            seatBookView.markAsTransparentSeat(this as TextView)
            this.setBackgroundResource(R.drawable.bg_chair_spacing)
            this.setPadding(5)
            this.setText("12")
        }
        seatBookView.getSeatView(88).apply {
            seatBookView.markAsTransparentSeat(this as TextView)
            this.setBackgroundResource(R.drawable.bg_chair_spacing)
            this.setPadding(5)
            this.setText("13")
        }

        seatBookView.setSeatClickListener(
            object : SeatClickListener {
                override fun onAvailableSeatClick(
                    selectedIdList: List<Int>,
                    view: View,
                ) {
                    view.setBackgroundResource(R.drawable.bg_selected)
                    view.tag = "P$seatCounter"
                    (view as TextView).text = "P$seatCounter"
                    view.setTextColor(resources.getColor(android.R.color.white))
                    seatCounter++
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
}
