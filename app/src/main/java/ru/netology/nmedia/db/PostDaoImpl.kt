package ru.netology.nmedia.db

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import ru.netology.nmedia.dto.Post

class PostDaoImpl(
    private val db: SQLiteDatabase
) : PostDao {

    // то же самое, что и Select
    override fun getAll() = db.query(
        PostsTable.NAME,
        PostsTable.ALL_COLUMN_NAMES,
        // условие после Where (мы никак не ограничиваем, антипаттен)
        null, null, null, null,
        //сортировка всех записей
        "${PostsTable.Column.ID.columnName} DESC"
    ).use { cursor ->
        List(cursor.count) {
            cursor.moveToNext()
            cursor.toPost()
        }
    }

    override fun save(post: Post): Post {
        // заполняем ContentValues, это спец тип, позволяющий задавать значения,
        // которые потом будут использоваться в insert/update.
        val values = ContentValues().apply {
            put(PostsTable.Column.AUTHOR.columnName, "Me")
            put(PostsTable.Column.CONTENT.columnName, post.content)
            put(PostsTable.Column.PUBLISHED.columnName, "Now")
        }
        val id = if (post.id != 0L) { // значит, что пост существует
            db.update(
                PostsTable.NAME,
                values,
                // что мы хотим обновить
                "${PostsTable.Column.ID.columnName} = ?", // PlaceHolder - держат место для ввода реальных аргс
                // значения ? берутся из массива ид
                arrayOf(post.id.toString())
            )
            post.id
        } else { // (post.id == 0L)
            // вставляем новую запись
            db.insert(PostsTable.NAME, null, values)
        }
        return db.query(
            PostsTable.NAME,
            PostsTable.ALL_COLUMN_NAMES,
            // условие после Where
            "${PostsTable.Column.ID.columnName} = ?",
            arrayOf(id.toString()), null, null, null, null // сортировка не нужна
        ).use { cursor ->
            cursor.moveToNext()
            cursor.toPost()
        }
    }

    override fun like(id: Long) {
        db.execSQL(
            """
           UPDATE ${PostsTable.NAME} SET
               likes = likes + CASE WHEN likedByMe THEN -1 ELSE 1 END,
               likedByMe = CASE WHEN likedByMe THEN 0 ELSE 1 END
           WHERE id = ?;
        """.trimIndent(), arrayOf(id)
        )
    }

    override fun delete(id: Long) {
        db.delete(
            PostsTable.NAME,
            "${PostsTable.Column.ID.columnName} = ?",
            // создаем массив из списка аргументов(знвчений), кот мы передаем вместо ?,
            // где каждый аргумент будет проверяться на лигитивность/правильность,
            // иначе можно криво удалить таблицу и сломать все приложение
            arrayOf(id.toString())
        )
    }

    override fun share(id: Long) {
        db.execSQL(
            """
                UPDATE ${PostsTable.NAME} SET
                shares = shares + CASE WHEN shared THEN 0 ELSE 1 END,
                shared = CASE WHEN shared THEN 1 ELSE 0 END
                WHERE id = ?;
        """.trimIndent(), arrayOf(id)
        )
    }
}
