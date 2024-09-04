package com.example

import app.cash.sqldelight.ColumnAdapter
import app.cash.sqldelight.driver.jdbc.asJdbcDriver
import com.example.plugins.*
import com.expediagroup.graphql.server.ktor.GraphQL
import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.application.*
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import java.time.Instant
import javax.sql.DataSource

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}


val koinModule = module {

    single {
        HikariDataSource().apply {
            jdbcUrl = "jdbc:postgresql://localhost:5432/mydb"
            username = "myuser"
            password = "mypass"
            maximumPoolSize = 3
        }
    }

    single {
        Database(get<DataSource>().asJdbcDriver())
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
