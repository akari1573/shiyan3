package com.example.myapplication

import android.os.Bundle
import android.os.SystemClock
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityFourthBinding

class FourthActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFourthBinding

    private var isRunning = false
    private var accumulatedMillis = 0L
    private var baseTime = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFourthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState != null) {
            isRunning = savedInstanceState.getBoolean(getString(R.string.key_running), false)
            accumulatedMillis = savedInstanceState.getLong(getString(R.string.key_accumulated), 0L)
            baseTime = savedInstanceState.getLong(getString(R.string.key_base), SystemClock.elapsedRealtime())
        } else {
            baseTime = SystemClock.elapsedRealtime()
        }

        binding.chronometer.base = baseTime
        binding.chronometer.setOnChronometerTickListener { updateDisplay() }

        binding.btnStart.setOnClickListener { start() }
        binding.btnPause.setOnClickListener { pause() }
        binding.btnReset.setOnClickListener { reset() }

        if (isRunning) {
            binding.chronometer.start()
        } else {
            updateDisplay()
        }
    }

    private fun start() {
        if (isRunning) {
            return
        }
        baseTime = SystemClock.elapsedRealtime() - accumulatedMillis
        binding.chronometer.base = baseTime
        binding.chronometer.start()
        isRunning = true
    }

    private fun pause() {
        if (!isRunning) {
            return
        }
        accumulatedMillis = SystemClock.elapsedRealtime() - baseTime
        binding.chronometer.stop()
        isRunning = false
        updateDisplay()
    }

    private fun reset() {
        binding.chronometer.stop()
        isRunning = false
        accumulatedMillis = 0L
        baseTime = SystemClock.elapsedRealtime()
        binding.chronometer.base = baseTime
        updateDisplay()
    }

    private fun updateDisplay() {
        val elapsed = if (isRunning) {
            SystemClock.elapsedRealtime() - baseTime
        } else {
            accumulatedMillis
        }
        val totalSeconds = elapsed / 1000
        val hours = totalSeconds / 3600
        val minutes = totalSeconds % 3600 / 60
        val seconds = totalSeconds % 60
        binding.chronometer.text = getString(R.string.time_format, hours, minutes, seconds)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(getString(R.string.key_running), isRunning)
        outState.putLong(getString(R.string.key_accumulated), accumulatedMillis)
        outState.putLong(getString(R.string.key_base), baseTime)
    }
}
