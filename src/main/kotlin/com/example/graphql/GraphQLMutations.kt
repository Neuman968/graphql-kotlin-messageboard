package com.example.graphql

import com.example.Author
import com.example.service.AuthorService
import com.expediagroup.graphql.server.operations.Mutation

class GraphQLMutations(private val authorService: AuthorService): Mutation {

    fun addAuthor(author: Author): Author = authorService.addAuthor(author)
}