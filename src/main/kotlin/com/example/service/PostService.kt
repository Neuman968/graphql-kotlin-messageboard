package com.example.service

import com.example.Post
import com.example.PostQueries

class PostService(private val postQueries: PostQueries) {
    fun getRecentPosts(limit: Int, offset: Int): List<Post> = postQueries.selectRecentPosts(limit.toLong(), offset.toLong())
        .executeAsList()
}