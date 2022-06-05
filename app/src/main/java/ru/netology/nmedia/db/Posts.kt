package ru.netology.nmedia.db

import android.database.Cursor
import ru.netology.nmedia.dto.Post

// делаем ф-цию расширения на курсоре

fun Cursor.toPost() = Post(
    // мы не можем из курсора вытащить данные по названию колонки, а только по ее ид(или индексу)
    id = getLong(getColumnIndexOrThrow(PostsTable.Column.ID.columnName)),
    author = getString(getColumnIndexOrThrow(PostsTable.Column.AUTHOR.columnName)),
    content = getString(getColumnIndexOrThrow(PostsTable.Column.CONTENT.columnName)),
    published = getString(getColumnIndexOrThrow(PostsTable.Column.PUBLISHED.columnName)),
    likes = getInt(getColumnIndexOrThrow(PostsTable.Column.LIKES.columnName)),
    // если нам придет инт, кот != 0, то запишется в true, все, что == 0, то запишется в false
    likedByMe = getInt(getColumnIndexOrThrow(PostsTable.Column.LIKED_BY_ME.columnName)) != 0,
    shares = getInt(getColumnIndexOrThrow(PostsTable.Column.SHARES.columnName)),
    shared = getInt(getColumnIndexOrThrow(PostsTable.Column.SHARED.columnName)) != 0
)