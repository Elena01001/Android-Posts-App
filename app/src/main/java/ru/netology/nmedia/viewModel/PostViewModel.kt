package ru.netology.nmedia.viewModel

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.netology.nmedia.adapter.PostInteractionListener
import ru.netology.nmedia.data.PostRepository
import ru.netology.nmedia.data.impl.InMemoryPostRepository
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.util.SingleLiveEvent

class PostViewModel : ViewModel(), PostInteractionListener {

    private val repository: PostRepository = InMemoryPostRepository()
    val data get() = repository.data

    val sharePostContent = SingleLiveEvent<String>()
    val navigateToPostContentScreenEvent = SingleLiveEvent<Unit>()

    val currentPost = MutableLiveData<Post?>(null)

    fun onSaveButtonClicked(content: String) { // нужно научить, когда пришел новый пост, а когда неновый для редактирования контента
        if (content.isBlank()) return
        val newPost = currentPost.value?.copy( // создание копии поста с новым контентом
            content = content
        ) ?: Post(
            id = PostRepository.NEW_POST_ID,
            author = "Me",
            content = content,
            published = "now"
        )
        repository.save(newPost)
        currentPost.value = null // сброс контента сохраненного поста в строке, где мы его печатали
    }

    fun onCancelButtonClicked() {
        currentPost.value = null
    }

    fun onAddButtonClicked(){
        navigateToPostContentScreenEvent.call()
    }

    override fun onLikeButtonClicked(post: Post) = repository.like(post.id)
    override fun onShareButtonClicked(post: Post) {
        sharePostContent.value = post.content
        repository.share(post.id)
    }
    override fun onRemoveButtonClicked(post: Post) = repository.delete(post.id)
    override fun onEditButtonClicked(postContent: String) {
        navigateToPostContentScreenEvent.call() // отобразится контент текущего поста на экране
    }

}
