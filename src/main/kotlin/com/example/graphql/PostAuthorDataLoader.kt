package com.example.graphql

import com.example.Author
import com.example.AuthorQueries
import com.example.Post
import com.expediagroup.graphql.dataloader.KotlinDataLoader
import graphql.GraphQLContext
import graphql.schema.DataFetcher
import graphql.schema.DataFetchingEnvironment
import org.dataloader.DataLoader
import org.dataloader.DataLoaderFactory
import java.util.concurrent.CompletableFuture

const val POST_AUTHOR_DATA_LOADER = "POST_AUTHOR"

class PostAuthorDataLoader(private val authorQueries: AuthorQueries, override val dataLoaderName: String) : KotlinDataLoader<Int, Author> {
    override fun getDataLoader(graphQLContext: GraphQLContext): DataLoader<Int, Author> {
        return DataLoaderFactory.newDataLoader{ ids ->
            CompletableFuture.supplyAsync {
                authorQueries.selectAllByIds(ids).executeAsList()
            }
        }
    }

}

class PostAuthorDataFetcher(): DataFetcher<CompletableFuture<Author>> {
    override fun get(environment: DataFetchingEnvironment): CompletableFuture<Author> {
        val authorId = environment.getSource<Post>()?.author_id
        return environment.getDataLoader<Int, Author>(POST_AUTHOR_DATA_LOADER)!!.load(authorId!!)
    }
}
/*

class CompanyDataFetcher : DataFetcher<CompletableFuture<Company>>, BeanFactoryAware {
    private lateinit var beanFactory: BeanFactory

    override fun setBeanFactory(beanFactory: BeanFactory) {
        this.beanFactory = beanFactory
    }

    override fun get(environment: DataFetchingEnvironment): CompletableFuture<Company> {
        val companyId = environment.getSource<Employee>().companyId
        return environment
            .getDataLoader<Int, Company>("companyLoader")
            .load(companyId)
    }
}
 */