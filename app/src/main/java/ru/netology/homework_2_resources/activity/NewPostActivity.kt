package ru.netology.homework_2_resources.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContract
import ru.netology.homework_2_resources.databinding.ActivityNewPostBinding

class NewPostActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityNewPostBinding.inflate(layoutInflater)

        setContentView(binding.root)

        val incomingText = intent?.getStringExtra(Intent.EXTRA_TEXT) // смотрим на входящий текст
        binding.content.setText(incomingText)

        binding.ok.setOnClickListener {
            val text = binding.content.text.toString()
            if (text.isBlank()) {
                setResult(RESULT_CANCELED)
            } else {
                setResult(RESULT_OK, Intent().apply {
                putExtra(Intent.EXTRA_TEXT, text)})
            }
            finish()
        }
    }

    object Contract: ActivityResultContract<String, String?>() {
        override fun createIntent(context: Context, input: String) =
            Intent(context, NewPostActivity::class.java).putExtra(Intent.EXTRA_TEXT, input) // создаем новый интент и тут же передаем в него переменную input типа String

        override fun parseResult(resultCode: Int, intent: Intent?) = intent?.getStringExtra(Intent.EXTRA_TEXT)

    }
}