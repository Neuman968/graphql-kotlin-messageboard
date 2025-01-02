package com.example.graphql

import com.example.Post
import com.expediagroup.graphql.generator.execution.KotlinDataFetcherFactoryProvider
import graphql.schema.DataFetcherFactory
import kotlin.reflect.KClass
import kotlin.reflect.KFunction

// Data Fetchers are loaded once at startup.
class ExtraFunctionsDataFetcherFactory(private val defaultDataFetcherFactoryProvider: KotlinDataFetcherFactoryProvider)
    : KotlinDataFetcherFactoryProvider by defaultDataFetcherFactoryProvider {

//    val lookupFunction: Map<KClass<*>, List<KFunction<*>>> = mapOf()

    override fun functionDataFetcherFactory(
        target: Any?,
        kClass: KClass<*>,
        kFunction: KFunction<*>
    ): DataFetcherFactory<Any?> {

        if (kClass == Post::class) {
            return defaultDataFetcherFactoryProvider.functionDataFetcherFactory(target, kClass, kFunction)
        }

       return defaultDataFetcherFactoryProvider.functionDataFetcherFactory(target, kClass, kFunction)
    }
}