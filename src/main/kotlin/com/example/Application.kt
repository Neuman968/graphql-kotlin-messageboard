package com.example

import app.cash.sqldelight.driver.jdbc.asJdbcDriver
import com.example.graphql.GraphQLQueries
import com.example.graphql.localDateTimeScalar
import com.example.plugins.configureRouting
import com.example.service.AuthorService
import com.example.service.PostService
import com.expediagroup.graphql.generator.hooks.SchemaGeneratorHooks
import com.expediagroup.graphql.server.ktor.GraphQL
import com.generated.example.Database
import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.application.*
import org.koin.dsl.module
import org.koin.ktor.ext.get
import org.koin.ktor.plugin.Koin
import java.time.LocalDateTime
import kotlin.reflect.KClass
import kotlin.reflect.KType

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}


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
        Database(get<HikariDataSource>().asJdbcDriver()).apply {
            Database.Schema.create(get<HikariDataSource>().asJdbcDriver())
        }
    }

    single {
        AuthorService(get<Database>().authorQueries)
    }

    single {
        PostService()
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
            }
        }
    }
    configureRouting()
}
