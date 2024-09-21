package com.example.service

import com.example.Author
import com.example.AuthorQueries
import com.example.Post
import com.expediagroup.graphql.server.extensions.getValuesFromDataLoader
import graphql.schema.DataFetchingEnvironment
import java.util.concurrent.CompletableFuture

class AuthorService(private val authorQueries: AuthorQueries) {
    fun getAuthors(): List<Author> = authorQueries.selectAll().executeAsList()

    fun addAuthor(author: Author): Author = author.copy(id = authorQueries.insert(author).executeAsOne())
}

fun Author.posts(dataFetchingEnvironment: DataFetchingEnvironment): CompletableFuture<List<Post>> {
    return dataFetchingEnvironment.getValuesFromDataLoader("author-posts", listOf(id))
}