package ru.netology.nmedia.db

import ru.netology.nmedia.dto.Post

// Всю непосредственную работу по запросам мы вынесем в DAO (Data Access Object)

interface PostDao {

    fun getAll(): List<Post>
    fun save(post: Post): Post
    fun like(id: Long)
    fun delete(id: Long)
    fun share(id: Long)
}