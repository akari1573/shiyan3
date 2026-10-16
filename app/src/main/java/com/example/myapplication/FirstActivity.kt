package com.example.myapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityFirstBinding

class FirstActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFirstBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFirstBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val name = intent.getStringExtra(getString(R.string.key_name))
        val id = intent.getStringExtra(getString(R.string.key_id))
        val age = intent.getIntExtra(getString(R.string.key_age), 0)
        binding.tvParams.text = getString(R.string.received_format, name, id, age)
    }
}
