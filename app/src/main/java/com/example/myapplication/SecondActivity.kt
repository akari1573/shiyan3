package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivitySecondBinding

class SecondActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySecondBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySecondBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnConfirm.setOnClickListener {
            val content = binding.etInput.text.toString().trim()
            if (content.isEmpty()) {
                Toast.makeText(this, R.string.empty_input_toast, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val data = Intent().putExtra(getString(R.string.key_result), content)
            setResult(RESULT_OK, data)
            finish()
        }
    }
}
