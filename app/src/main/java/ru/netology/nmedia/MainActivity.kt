package ru.netology.nmedia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.DrawableRes
import ru.netology.nmedia.adapter.PostsAdapter
import ru.netology.nmedia.data.PostRepository
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.util.hideKeyboard
import ru.netology.nmedia.viewModel.PostViewModel


class MainActivity : AppCompatActivity() {

    private val viewModel: PostViewModel by viewModels() // делегирование для того, чтобы при перевороте экрана не сбрасывался текст

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = PostsAdapter(viewModel)

        binding.postsRecyclerView.adapter = adapter

        // повесили наблюдателя за отрисовкой поста, как только в посте изменятся данные,
        // то вызовется лямбда с ф-цией рендера, кот создана ниже
        viewModel.data.observe(this) { posts ->
            adapter.submitList(posts)
        }

        binding.saveButton.setOnClickListener {
            val content = binding.contentEditText.text.toString()
            viewModel.onSaveButtonClicked(content)
            binding.contentEditText.clearFocus() // убираем курсор из пустой строки после нажатия кнопки сохранить
            binding.contentEditText.hideKeyboard()
        }

        binding.cancelButton.setOnClickListener {
            viewModel.onCancelButtonClicked()
        }

        viewModel.currentPost.observe(this) { currentPost ->
            run {
                binding.contentEditText.setText(currentPost?.content) // наблюдение за удалением лишнего контента после сохранения поста
                binding.editedContentLine.text = currentPost?.content
                if (currentPost?.content != null) {
                    binding.contentEditText.requestFocus()
                    binding.group.visibility = View.VISIBLE
                } else {
                    binding.contentEditText.clearFocus() // убираем курсор из пустой строки после нажатия кнопки сохранить
                    binding.contentEditText.hideKeyboard() // можно кликнуть и посмотреть, как мы сделали расширение
                    binding.group.visibility = View.GONE
                }
            }
        }
    }
}



