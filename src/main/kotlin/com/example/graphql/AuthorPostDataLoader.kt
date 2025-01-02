package com.example.graphql

import com.example.Post
import com.example.PostQueries
import com.expediagroup.graphql.dataloader.KotlinDataLoader
import graphql.GraphQLContext
import org.dataloader.DataLoader
import org.dataloader.DataLoaderFactory
import java.util.concurrent.CompletableFuture


/*
val UniversityDataLoader = object : KotlinDataLoader<Int, University?> {
    override val dataLoaderName = "UNIVERSITY_LOADER"
    override fun getDataLoader(graphQLContext: GraphQLContext) =
        DataLoaderFactory.newDataLoader { ids ->
            CompletableFuture.supplyAsync {
                runBlocking { University.search(ids).toMutableList() }
            }
        }
}
 */

//class AuthorPostDataLoader(private val postQueries: PostQueries, override val dataLoaderName: String) : KotlinDataLoader<Int, Post> {
//    override fun getDataLoader(graphQLContext: GraphQLContext): DataLoader<Int, Post> {
//        DataLoaderFactory.newDataLoader { ids ->
//            CompletableFuture.supplyAsync {
//                postQueries.selectPostsForAuthors(ids).executeAsList()
//            }
//        }
//    }
//
//}
