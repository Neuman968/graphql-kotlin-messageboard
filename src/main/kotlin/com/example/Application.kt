package com.example

import app.cash.sqldelight.driver.jdbc.asJdbcDriver
import com.example.graphql.GraphQLQueries
import com.example.plugins.configureRouting
import com.example.service.AuthorService
import com.example.service.PostService
import com.expediagroup.graphql.server.ktor.GraphQL
import com.generated.example.Database
import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.application.*
import org.koin.dsl.module
import org.koin.ktor.ext.get
import org.koin.ktor.plugin.Koin

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}


val koinModule = module {

    single {
        HikariDataSource().apply {
            jdbcUrl = "jdbc:postgresql://localhost:5432/messageboard"
            username = "messageboard"
            password = "password"
            maximumPoolSize = 3
        }
    }

    single {
        Database(get<HikariDataSource>().asJdbcDriver())
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
        }
    }
    configureRouting()
}
