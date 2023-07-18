package com.ezgieren.ketchthedollarwithkotlin

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.ezgieren.ketchthedollarwithkotlin.databinding.ActivityMainBinding
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    var timeNumber = 0
    private var scoreNumber = 0
    private var runnable = Runnable {}
    var handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        startTime()
        clickToImage()
    }

    override fun onStart() {
        super.onStart()
        animateToImage()

        binding.imageViewDollar.postDelayed({
            binding.imageViewDollar.visibility = View.GONE
        }, 10000)
    }

    private fun randomPosition() = Random.nextInt(-500, 500).toFloat()

    private fun animateToImage() {
        binding.imageViewDollar.animate()
            .translationX(randomPosition())
            .translationY(randomPosition())
            .setDuration(100)
            .withEndAction(::animateToImage)
            .start()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun clickToImage() {
        binding.imageViewDollar.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                val touchPoint = event.edgeFlags.dec() * -1
                scoreNumber += touchPoint
                binding.scoreTextView.text = "Score: $scoreNumber"
            }
            false
        }
    }

    private fun alertShow() {
        val alertCreate = AlertDialog.Builder(this)
        alertCreate.setTitle("Game Over")
        alertCreate.setMessage("Restart The Game?")

        alertCreate.setPositiveButton("Yes") { _, _ ->
            Toast.makeText(this@MainActivity, "Ready ? Go!", Toast.LENGTH_LONG).show()
            //Restart
            val intent = intent
            finish()
            startActivity(intent)
        }
        alertCreate.setNegativeButton("No") { _, _ ->
            Toast.makeText(this@MainActivity, "Game Over!", Toast.LENGTH_LONG).show()
        }

        alertCreate.create().show()
    }

    private fun startTime() {
        timeNumber = 0
        runnable = Runnable {
            if (timeNumber < 10) {
                timeNumber++
                binding.timeTextView.text = "Time: ${timeNumber}"
                handler.postDelayed(runnable, 1000)
            } else if (timeNumber == 10) {
                stopTime()
                alertShow()
            }
        }
        handler.post(runnable)
    }

    private fun stopTime() {
        binding.timeTextView.text = "Time: 0"
        handler.removeCallbacks(runnable)
    }
}

