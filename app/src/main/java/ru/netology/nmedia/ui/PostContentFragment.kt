package ru.netology.nmedia.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContract
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import ru.netology.nmedia.databinding.PostContentActivityBinding


// Здесь мý создаём Explicit Intent, указывая, объект какого класса его
//должен обрабатывать. И мы не только запускаем Activity, мý ещё
//запускаем его с требованием вернуть нам назад результат (т.е. тот
//текст, которýй введёт пользователь)

class PostContentFragment : Fragment() {

    private val textToEdit
        get() = requireArguments().getString(TEXT_TO_EDIT_ARGUMENTS_KEY)


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = PostContentActivityBinding.inflate(layoutInflater, container, false).also { binding ->
        // обращаемся к тексту для ред-я, с которым фрагмент был запущен, и достаем данные, переданные через аргумент
        binding.edit.setText(textToEdit)
        //val textToEdit = intent?.extras?.getString(Intent.EXTRA_TEXT) ?: ""
        //binding.edit.setText(textToEdit)
        binding.edit.requestFocus() // как только откроется экран, то курсор поставится на поле edit
        // при нажатии на кнопку ОК формируем пустой интент
        binding.ok.setOnClickListener {
            onOkButtonClicked(binding)
        }
    }.root

    private fun onOkButtonClicked(binding: PostContentActivityBinding) {
        if (!binding.edit.text.isNullOrBlank()) {
            val resultBundle = Bundle(1)
            // конвертируем текст в строку, укладываем в заготовленный бандл
            resultBundle.putString(RESULT_KEY, binding.edit.text.toString())
            setFragmentResult(REQUEST_KEY, resultBundle)
        }
        parentFragmentManager.popBackStack() // навигируемся назад, фрагмент выкидывается из backstackа фрагментов
    }

    // чтобы различать 2 рез-та из 2 разных фрагментов
    companion object {
        private const val TEXT_TO_EDIT_ARGUMENTS_KEY = "textToEdit"
        const val REQUEST_KEY = "requestKey"
        const val RESULT_KEY = "postNewContent"

        fun createInstance(textToEdit: String?) = PostContentFragment().apply {
            arguments = Bundle(1).also {
                it.putString(TEXT_TO_EDIT_ARGUMENTS_KEY, textToEdit)
            }
        }
    }


}