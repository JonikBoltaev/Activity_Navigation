package ru.jonik.activityresult

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContract
import ru.jonik.activityresult.databinding.ActivitySecondBinding

class SecondActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySecondBinding

    // Реализовано при помощи getter. Каждый раз когда обращаемся к resultIntent
    // Создается Intent внутрь которого по ключу EXTRA_OUTPUT_MESSAGE складывается текстовая строка
    // Которую пользователь отредактировал в edit text
    private val resultIntent: Intent
        get() = Intent().apply {
            putExtra(EXTRA_OUTPUT_MESSAGE, binding.edValue.text.toString())
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySecondBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSave.setOnClickListener { onSavePressed() }

        // Системный метод onBackPressed
        binding.btnCancel.setOnClickListener { onBackPressed() }

        // Назначаем входное значение в edit text
        binding.edValue.setText(intent.getStringExtra(EXTRA_INPUT_MESSAGE))
    }

    private fun onSavePressed() {
        setResult(RESULT_OK, resultIntent)
        finish()
    }

    // Переопределяем системный метод для мониторинга (Не обязательно)
    override fun onBackPressed() {
        setResult(RESULT_CANCELED, resultIntent)
        super.onBackPressed()
    }


    data class Output(
        val message: String,
        val confirmed: Boolean
    )

    // Кастомный контракт
    // Каждый контракт должен наследоваться от ActivityResultContract<Входящее значение для активити, Результат в предыдущую активити>
    class Contract : ActivityResultContract<String, Output>() {

        //Метод для запуска активити
        override fun createIntent(context: Context, input: String) =
            Intent(context, SecondActivity::class.java).apply {
                putExtra(EXTRA_INPUT_MESSAGE, input)
            }

        //Берет результат запуска активити
        override fun parseResult(resultCode: Int, intent: Intent?): Output? {
            if (intent == null) return null
            val message = intent.getStringExtra(EXTRA_OUTPUT_MESSAGE) ?: return null

            val confirmed = resultCode == RESULT_OK
            return Output(message, confirmed)
        }
    }

    companion object {
        private const val EXTRA_INPUT_MESSAGE = "EXTRA_INPUT_MESSAGE"
        private const val EXTRA_OUTPUT_MESSAGE = "EXTRA_OUTPUT_MESSAGE"
    }
}