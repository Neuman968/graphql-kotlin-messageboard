package com.example

import com.example.plugins.*
import com.expediagroup.graphql.server.ktor.GraphQL
import io.ktor.server.application.*
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

val koinModule = module {
    single {
        Database(JdbcSqliteDriver("jdbc:sqlite:build/databases/database.db"))
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
//                HelloWorldQuery()
            )
        }
    }
    configureRouting()
}
