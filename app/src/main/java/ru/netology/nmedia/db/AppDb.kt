package ru.netology.nmedia.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase

// Ключевая идея: мы хотим получить синглтон на все приложение для доступа к БД (через метод getInstance),
// Это некая симуляция нашей БД, кот принимает на вход другую БД
// Когда мы создадим экземпляр AppDb, то получим доступ к постДао и создадим репозиторий

class AppDb private constructor(db: SQLiteDatabase) {
    val postDao: PostDao = PostDaoImpl(db)

    companion object {
        @Volatile
        private var instance: AppDb? = null

        fun getInstance(context: Context): AppDb {
            return instance ?: synchronized(this) {
                instance ?: AppDb(
                    buildDatabase(context, arrayOf(PostsTable.DDL))
                ).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context, DDLs: Array<String>) = DbHelper(
            context, 1, "app.db", DDLs,
        ).writableDatabase
    }
}