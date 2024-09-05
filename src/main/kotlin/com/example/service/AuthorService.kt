package com.example.service

import com.example.Author
import com.example.AuthorQueries

class AuthorService(private val authorQueries: AuthorQueries) {
    fun getAuthors(): List<Author> = authorQueries.selectAll().executeAsList()

    fun addAuthor(author: Author): Author = author.copy(id = authorQueries.insert(author).executeAsOne())
}