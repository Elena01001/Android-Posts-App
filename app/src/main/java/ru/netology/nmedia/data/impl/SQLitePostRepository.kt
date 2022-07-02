package ru.netology.nmedia.data.impl

import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.data.PostRepository
import ru.netology.nmedia.db.PostDao
import ru.netology.nmedia.dto.Post

/*
Репозиторий "кеширует" в памяти данные для ускорения доступа.
Поскольку у нас с базой работает только он (через DAO), то это допустимо.
*/

class SQLitePostRepository(
    private val dao: PostDao
) : PostRepository {

    private val posts //= emptyList<Post>()
        get() = checkNotNull(data.value) {
            "Data value should not be null"
        }

    override val data = MutableLiveData(dao.getAll())

    override fun save(post: Post) {
        val id = post.id
        val saved = dao.save(post)
        data.value = if (id == 0L) {
            listOf(saved) + posts
        } else {
            posts.map {
                if (it.id != id) it else saved
            }
        }
    }

    override fun share(id: Long) {
        dao.share(id)
        data.value = posts.map { it ->
            if (it.id == id) {
                it
                    .copy()
                    .also { if (it.shared) it.shares else it.shares++ }
            } else it
        }
    }

    override fun like(id: Long) {
        dao.like(id)
        data.value = posts.map { it ->
            if (it.id == id) {
                it
                    .copy(likedByMe = !it.likedByMe)
                    .also { if (it.likedByMe) it.likes++ else it.likes-- }
            } else it
        }
    }

    override fun delete(id: Long) {
        dao.delete(id)
        data.value = posts.filter { it.id != id }
    }
}