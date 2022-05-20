package ru.netology.nmedia.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.launch
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.databinding.PostContentActivityBinding
import ru.netology.nmedia.viewModel.PostViewModel


// Здесь мý создаём Explicit Intent, указýвая, объект какого класса его
//должен обрабатывать. И мы не только запускаем Activity, мý ещё
//запускаем его с требованием вернуть нам назад результат (т.е. тот
//текст, которýй введёт пользователь)

class PostContentActivity : AppCompatActivity() {

    private val viewModel: PostViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = PostContentActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.edit.requestFocus() // как только откроется экран, то курсор поставится на поле edit
        // при нажатии на кнопку ОК формируем пустой интент
        binding.ok.setOnClickListener {
            val intent = Intent()
            if (binding.edit.text.isNullOrBlank()) {
                // если в поле ввода текста пусто, то рез-т выполнения активити отменить
                // и вкладываем пустой интент
                setResult(Activity.RESULT_CANCELED, intent)
            } else {
                // если все хорошо, то конвертируем текст в строку, укладываем в заготовленный интент
                val content = binding.edit.text.toString()
                intent.putExtra(RESULT_KEY, content)
                setResult(Activity.RESULT_OK, intent)
            }
            finish()
        }

        val mainActivityLauncher = registerForActivityResult(
            MainActivity.EditingResultContract
        ) { postContent ->
            postContent ?: return@registerForActivityResult
            viewModel.onEditButtonClicked(postContent)
        }

        viewModel.currentPost.observe(this) {
            mainActivityLauncher.launch(RESULT_KEY)
        }
    }

    // Для создания Intent и обработки возвращаемого результата
    object ResultContract : ActivityResultContract<Unit, String?>() {

        // метод возвращает интент, с кот запутсится наша активити
        override fun createIntent(context: Context, input: Unit) =
            Intent(context, PostContentActivity::class.java)

        override fun parseResult(resultCode: Int, intent: Intent?) =
            if (resultCode == Activity.RESULT_OK) {
                // расковыриваем интент
                intent?.getStringExtra(RESULT_KEY)
            } else null
    }

    companion object {
        const val RESULT_KEY = "postNewContent"
    }


}