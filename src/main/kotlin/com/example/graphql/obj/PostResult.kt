package com.example.graphql.obj

import com.example.Author
import com.example.Post
import com.expediagroup.graphql.server.extensions.getValueFromDataLoader
import graphql.schema.DataFetchingEnvironment
import java.util.concurrent.CompletableFuture

data class PostResult(
    val post: Post
) {
    fun author(dataFetchingEnvironment: DataFetchingEnvironment): CompletableFuture<Author> {
        return dataFetchingEnvironment.getValueFromDataLoader<Int, Author>("POST_AUTHOR", post.id)
    }
}