package com.example.service

import com.example.Author
import com.example.Post
import com.example.PostQueries
import com.expediagroup.graphql.server.extensions.getValueFromDataLoader
import graphql.schema.DataFetchingEnvironment
import java.util.concurrent.CompletableFuture

class PostService(private val postQueries: PostQueries) {
    fun getRecentPosts(limit: Int, offset: Int): List<Post> = postQueries.selectRecentPosts(limit.toLong(), offset.toLong())
        .executeAsList()
}