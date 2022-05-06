package ru.netology.nmedia.dto

data class Post(
    val id: Long,
    val author: String,
    val content: String,
    val published: String,
    var likes: Int = 999,
    var shares: Int = 5500,
    var viewings: Int = 1000000,
    var likedByMe: Boolean = false,
    var shared: Boolean = false

)