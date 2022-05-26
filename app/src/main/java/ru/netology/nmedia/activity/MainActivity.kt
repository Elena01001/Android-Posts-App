package ru.netology.nmedia.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.launch
import androidx.activity.viewModels
import ru.netology.nmedia.R
import ru.netology.nmedia.adapter.PostsAdapter
import ru.netology.nmedia.databinding.ActivityMainBinding
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

        binding.fab.setOnClickListener {
            viewModel.onAddButtonClicked()
        }

        viewModel.sharePostContent.observe(this) { postContent ->
            val intent = Intent().apply {
                action = Intent.ACTION_SEND  // создаем пустой интент и заполняем его через apply
                putExtra(Intent.EXTRA_TEXT, postContent) // кладем некоторые данные, кот будем share
                type = "text/plain"
            }
            val shareIntent =
                Intent.createChooser(
                    intent,
                    getString(R.string.description_post_share) // создаем меню выбора
                )
            startActivity(shareIntent)
        }

        viewModel.videoPlayEvent.observe(this) { videoLink ->
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(videoLink))
            // если у-во пользователя может выполнить данную задачу, то запускаем этот интент
            if (intent.resolveActivity(packageManager) != null) {
                startActivity(intent)
            }
        }

        // показываем новый экран в нашем приложении
        // данная ф-ция будет вызвана при завершении PostContentActivity
        val postContentActivityLauncher = registerForActivityResult(
            PostContentActivity.ResultContract
        ) { postContent ->
            postContent ?: return@registerForActivityResult
            viewModel.onSaveButtonClicked(postContent)
        }
        viewModel.navigateToPostContentScreenEvent.observe(this) { postContent ->
            postContentActivityLauncher.launch(postContent)
        }
    }

}



