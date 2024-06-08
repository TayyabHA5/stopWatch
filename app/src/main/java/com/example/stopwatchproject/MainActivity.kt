package com.example.stopwatchproject

import android.app.Dialog
import android.os.Bundle
import android.os.SystemClock
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Chronometer
import android.widget.NumberPicker
import androidx.appcompat.app.AppCompatActivity
import com.example.stopwatchproject.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var isRunning = false
    private var minutes: Int = 0
    private lateinit var lapsAdapter : ArrayAdapter<String>
    private val lapsList: MutableList<String> = mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        lapsAdapter = ArrayAdapter(this,android.R.layout.simple_list_item_1,lapsList)
        binding.listView.adapter = lapsAdapter
        binding.imgClock.setOnClickListener {
            val dialog = Dialog(this)
            dialog.setContentView(R.layout.dialog)
            val numberPicker = dialog.findViewById<NumberPicker>(R.id.numberPicker)
            numberPicker.minValue = 0
            numberPicker.maxValue = 10
            val btnSetTime = dialog.findViewById<Button>(R.id.btnSetTime)
            btnSetTime.setOnClickListener {
                minutes = numberPicker.value
                binding.tvClockTime.text = "$minutes mins"
                dialog.dismiss()
            }
            dialog.show()
        }

        binding.btnRun.setOnClickListener {
            if (isRunning) {
                isRunning = false
                binding.tvChronometer.stop()
                binding.btnRun.text = "Run"
            } else {
                isRunning = true
                binding.btnRun.text = "Stop"
                if (minutes > 0) {
                    val totalMillis = minutes * 60 * 1000L
                    binding.tvChronometer.base = SystemClock.elapsedRealtime() + totalMillis
                    binding.tvChronometer.onChronometerTickListener = Chronometer.OnChronometerTickListener {
                        val elapsedMillis = binding.tvChronometer.base - SystemClock.elapsedRealtime()
                        if (elapsedMillis <= 0) {
                            binding.tvChronometer.stop()
                            isRunning = false
                            binding.btnRun.text = "Run"
                        }
                    }
                    binding.tvChronometer.start()
                } else {
                    binding.tvChronometer.base = SystemClock.elapsedRealtime()
                    binding.tvChronometer.start()
                }
            }
        }
        binding.btnLap.setOnClickListener(){
            if (isRunning){
                val elapsedMillis = SystemClock.elapsedRealtime() - binding.tvChronometer.base
                val minutes = (elapsedMillis / 1000) / 60
                val seconds = (elapsedMillis / 1000) % 60
                val millis = (elapsedMillis % 1000) / 10
                val lapTime = String.format("%02d:%02d:%02d", minutes, seconds, millis)
                lapsList.add(lapTime)
                lapsAdapter.notifyDataSetChanged()
            }
        }
    }
}
