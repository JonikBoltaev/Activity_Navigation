package ru.jonik.activityresult

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission

import ru.jonik.activityresult.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    // Для отправки запроса на создание новой активити надо вызвать метод registerForActivityResult
    // Принято вызывать registerForActivityResult в полях класса
    private val requestPermissionLauncher =
        registerForActivityResult(RequestPermission()) {
            Toast.makeText(this, "Permission granted $it", Toast.LENGTH_SHORT).show()
        }

    // Кастомный контракт
    private val editMessageLauncher =
        registerForActivityResult(SecondActivity.Contract()) {
            if (it != null && it.confirmed) {
                binding.tvText.text = it.message
            }
            Toast.makeText(this, "Edit result: $it", Toast.LENGTH_SHORT).show()
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnEdit.setOnClickListener { editMessage() }
        binding.btnRequestPermission.setOnClickListener { requestPermission() }
    }

    private fun requestPermission() {
        requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    private fun editMessage() {
        editMessageLauncher.launch(binding.tvText.text.toString())
    }
}