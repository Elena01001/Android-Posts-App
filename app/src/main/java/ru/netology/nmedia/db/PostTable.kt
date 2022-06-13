package ru.netology.nmedia.db

object PostsTable {

    const val NAME = "posts"

    // скрипт для создания таблицы в sqlite
    val DDL = """
        CREATE TABLE $NAME (
            ${Column.ID.columnName} INTEGER PRIMARY KEY AUTOINCREMENT,
            ${Column.AUTHOR.columnName} TEXT NOT NULL,
            ${Column.CONTENT.columnName} TEXT NOT NULL,
            ${Column.PUBLISHED.columnName} TEXT NOT NULL,
            ${Column.LIKED_BY_ME.columnName} BOOLEAN NOT NULL DEFAULT 0,
            ${Column.LIKES.columnName} INTEGER NOT NULL DEFAULT 0,
            ${Column.SHARES.columnName} INTEGER NOT NULL DEFAULT 0,
            ${Column.SHARED.columnName} BOOLEAN NOT NULL DEFAULT 0,
            ${Column.VIEWINGS.columnName} INTEGER NOT NULL DEFAULT 0,
            ${Column.VIDEOLINK.columnName} TEXT NOT NULL
        );
        """.trimIndent()
    /*  */

    // создаем список колонок в строковом виде в массиве
    val ALL_COLUMN_NAMES = Column.values().map { it.columnName }.toTypedArray()

    enum class Column(val columnName: String) {
        ID("id"),
        AUTHOR("author"),
        CONTENT("content"),
        PUBLISHED("published"),
        LIKED_BY_ME("likedByMe"),
        LIKES("likes"),
        SHARES("shares"),
        SHARED("shared"),
        VIEWINGS("viewings"),
        VIDEOLINK("videoLink")
    }

}