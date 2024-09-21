package com.example.graphql

import com.example.Post
import com.example.service.AuthorService
import com.example.service.PostService
import com.expediagroup.graphql.server.operations.Query

class GraphQLQueries(
    private val authorService: AuthorService,
    private val postService: PostService
) : Query {

    fun getAuthors() = authorService.getAuthors()

    fun getRecentPosts(limit: Int, offset: Int): List<Post> = postService.getRecentPosts(limit, offset)
}