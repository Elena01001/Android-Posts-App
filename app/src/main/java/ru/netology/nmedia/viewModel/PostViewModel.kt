package ru.netology.nmedia.viewModel

import androidx.lifecycle.ViewModel
import ru.netology.nmedia.data.PostRepository
import ru.netology.nmedia.data.impl.InMemoryPostRepository
import ru.netology.nmedia.dto.Post

class PostViewModel : ViewModel() {

    private val repository: PostRepository = InMemoryPostRepository()
    val data get() = repository.data
    fun onLikeButtonClicked(post: Post) = repository.like(post.id)
    fun onShareButtonClicked(post: Post) = repository.share(post.id)
}
