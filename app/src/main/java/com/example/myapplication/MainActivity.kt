package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val secondActivityLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val content = result.data?.getStringExtra(getString(R.string.key_result))
                if (!content.isNullOrEmpty()) {
                    binding.tvResult.text = getString(R.string.result_received, content)
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Log.d(TAG, getString(R.string.log_on_create))

        binding.btnFirst.setOnClickListener {
            val intent = Intent(this, FirstActivity::class.java)
            intent.putExtra(getString(R.string.key_name), getString(R.string.param_name))
            intent.putExtra(getString(R.string.key_id), getString(R.string.param_id))
            intent.putExtra(getString(R.string.key_age), resources.getInteger(R.integer.param_age))
            startActivity(intent)
        }

        binding.btnSecond.setOnClickListener {
            secondActivityLauncher.launch(Intent(this, SecondActivity::class.java))
        }

        binding.btnThird.setOnClickListener {
            startActivity(Intent(this, ThirdActivity::class.java))
        }

        binding.btnFourth.setOnClickListener {
            startActivity(Intent(this, FourthActivity::class.java))
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, getString(R.string.log_on_start))
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, getString(R.string.log_on_resume))
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, getString(R.string.log_on_pause))
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, getString(R.string.log_on_stop))
    }

    override fun onRestart() {
        super.onRestart()
        Log.d(TAG, getString(R.string.log_on_restart))
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, getString(R.string.log_on_destroy))
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.d(TAG, getString(R.string.log_on_save_instance))
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        Log.d(TAG, getString(R.string.log_on_restore_instance))
    }

    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }
}
