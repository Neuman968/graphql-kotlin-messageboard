package com.example

import app.cash.sqldelight.driver.jdbc.asJdbcDriver
import com.example.graphql.ExtraFunctionsDataFetcherFactory
import com.example.graphql.GraphQLQueries
import com.example.graphql.POST_AUTHOR_DATA_LOADER
import com.example.graphql.PostAuthorDataLoader
import com.example.graphql.localDateTimeScalar
import com.example.plugins.configureRouting
import com.example.service.AuthorService
import com.example.service.PostService
import com.expediagroup.graphql.dataloader.KotlinDataLoaderRegistryFactory
import com.expediagroup.graphql.generator.execution.SimpleKotlinDataFetcherFactoryProvider
import com.expediagroup.graphql.generator.hooks.SchemaGeneratorHooks
import com.expediagroup.graphql.server.ktor.GraphQL
import com.generated.example.Database
import com.zaxxer.hikari.HikariDataSource
import graphql.schema.DataFetcher
import graphql.schema.FieldCoordinates
import graphql.schema.GraphQLCodeRegistry
import graphql.schema.GraphQLFieldDefinition
import graphql.schema.GraphQLObjectType
import graphql.schema.GraphQLSchemaElement
import graphql.schema.GraphQLTypeReference
import io.ktor.server.application.*
import org.koin.dsl.module
import org.koin.ktor.ext.get
import org.koin.ktor.plugin.Koin
import java.time.LocalDateTime
import javax.sql.DataSource
import kotlin.reflect.KClass
import kotlin.reflect.KType

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

/**
 * Koin module used for dependency injection.
 */
val koinModule = module {

    single {
        HikariDataSource().apply {
            jdbcUrl = "jdbc:postgresql://localhost:5432/postgres"
            username = "postgres"
            password = "postgres"
            maximumPoolSize = 3
        }
    }

    single {
        // SQLDelight database singleton, used to access "Queries" Auto generated objects.
        Database(get<HikariDataSource>().asJdbcDriver()).apply {
            val dataSource = get<HikariDataSource>()
            val driver = dataSource.asJdbcDriver()
            // Replace with proper version and migration tracking.
            if (dataSource.dbNeedsCreate()) {
                Database.Schema.create(driver)
                Database.Schema.migrate(driver, 0, Database.Schema.version)
            }
        }
    }

    single {
        PostAuthorDataLoader(get<Database>().authorQueries, POST_AUTHOR_DATA_LOADER)
    }

    single {
        AuthorService(get<Database>().authorQueries)
    }

    single {
        PostService(get<Database>().postQueries)
    }

    single {
        GraphQLQueries(get<AuthorService>(), get<PostService>())
    }
}

fun Application.module() {
    install(Koin) {
        modules(koinModule)
    }
    install(GraphQL) {

        schema {

            packages = listOf("com.example")
            queries = listOf(
                get<GraphQLQueries>()
            )


            hooks = object : SchemaGeneratorHooks {
                override fun willGenerateGraphQLType(type: KType) = when (type.classifier as? KClass<*>) {
                    LocalDateTime::class -> localDateTimeScalar
                    else -> null
                }


                override fun onRewireGraphQLType(
                    generatedType: GraphQLSchemaElement,
                    coordinates: FieldCoordinates?,
                    codeRegistry: GraphQLCodeRegistry.Builder
                ): GraphQLSchemaElement {

                    if (generatedType is GraphQLObjectType) {
                        if (generatedType.name == Post::class.simpleName) {

//                            generatedType.fields.add(
//                                GraphQLFieldDefinition.newFieldDefinition()
//                                    .name("author")
//                                    .type(GraphQLTypeReference("Author"))
//                                    .build()
//                            )

                            codeRegistry.dataFetchers("Post", mapOf("author" to DataFetcher<Author> { env ->
                                println("Fetching author! ${env}")
                                Author(
                                    1,
                                    "John Doe",
                                    created_on = LocalDateTime.now(),
                                    updated_on = LocalDateTime.now()
                                )
                            }))

                        }
                    }
                    return super.onRewireGraphQLType(generatedType, coordinates, codeRegistry)
                }
//                override fun didGenerateGraphQLType(type: KType, generatedType: GraphQLType): GraphQLType {
//                    if (type == Post::class.starProjectedType) {
//                        val newField = GraphQLFieldDefinition.newFieldDefinition()
//                           .name("author")
//
////                           .type(Author::class)
//                           .dataFetcher()
////                           .dataFetcherFactory()
//                    }
//                    return super.didGenerateGraphQLType(type, generatedType)
//                }

            }


            engine {
                dataLoaderRegistryFactory = KotlinDataLoaderRegistryFactory(
                    get<PostAuthorDataLoader>()
                )
                dataFetcherFactoryProvider = ExtraFunctionsDataFetcherFactory(SimpleKotlinDataFetcherFactoryProvider())
            }
        }
    }
    configureRouting()
}

fun DataSource.dbNeedsCreate(): Boolean = try {
    !this.connection.createStatement().execute("SELECT * FROM author")
} catch (e: Exception) {
    true
}