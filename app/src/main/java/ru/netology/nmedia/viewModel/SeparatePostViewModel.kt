package ru.netology.nmedia.viewModel

import android.widget.PopupMenu
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.netology.nmedia.R
import ru.netology.nmedia.adapter.PostInteractionListener
import ru.netology.nmedia.adapter.PostsAdapter
import ru.netology.nmedia.adapter.SeparatePostListener
import ru.netology.nmedia.data.PostRepository
import ru.netology.nmedia.data.impl.InMemoryPostRepository
import ru.netology.nmedia.databinding.PostBinding
import ru.netology.nmedia.databinding.SeparatePostViewBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.util.SingleLiveEvent

class SeparatePostViewModel(
    private val separatedPost: Post,
    private val postInteractionListener: PostInteractionListener,
    binding: PostBinding
) : ViewModel(), SeparatePostListener {

    private val repository: PostRepository = InMemoryPostRepository
    val data get() = repository.data

    private val postViewHolder = PostsAdapter.ViewHolder(binding, postInteractionListener)

    init {
        postViewHolder.bind(separatedPost)
    }

    val sharePostContent = SingleLiveEvent<String>()
    val videoPlayEvent = SingleLiveEvent<String>()
    val navigateToPostContentScreenEvent = SingleLiveEvent<String?>()
    val currentPost = MutableLiveData<Post>()

    fun getPostById(postId: Long) : Post? =
        repository.getById(postId)

    fun onSaveButtonClicked(content: String) {
        val changedPost = currentPost.value?.copy( // создание копии поста с новым контентом
            content = content
        )
        if (changedPost != null) {
            repository.save(changedPost)
        } else return
    }

    override fun onLikeButtonClicked() = postInteractionListener.onLikeButtonClicked(separatedPost)

    override fun onShareButtonClicked() =
        postInteractionListener.onShareButtonClicked(separatedPost)

    override fun onRemoveButtonClicked() =
        postInteractionListener.onRemoveButtonClicked(separatedPost)

    override fun onEditButtonClicked() = postInteractionListener.onEditButtonClicked(separatedPost)

    override fun onVideoPlayButtonClicked() =
        postInteractionListener.onVideoPlayButtonClicked(separatedPost)

}