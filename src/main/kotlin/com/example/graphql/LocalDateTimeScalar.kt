package com.example.graphql

import graphql.schema.Coercing
import graphql.schema.CoercingParseLiteralException
import graphql.schema.CoercingParseValueException
import graphql.schema.CoercingSerializeException
import graphql.schema.GraphQLScalarType
import java.time.LocalDateTime

val localDateTimeScalar = GraphQLScalarType.newScalar()
    .name("LocalDateTime")
    .description("A type representing a formatted java.time.LocalDateTime")
    .coercing(LocalDateTimeCoercing)
    .build()


object LocalDateTimeCoercing : Coercing<LocalDateTime, String> {
    override fun parseValue(input: Any): LocalDateTime = runCatching {
        LocalDateTime.parse(serialize(input))
    }.getOrElse {
        throw CoercingParseValueException("Expected valid LocalDateTime but was $input")
    }

    override fun parseLiteral(input: Any): LocalDateTime {
        val localDateTimeString = (input as? String)?.toString()
        return runCatching {
            LocalDateTime.parse(localDateTimeString)
        }.getOrElse {
            throw CoercingParseLiteralException("Expected valid LocalDateTime literal but was $localDateTimeString")
        }
    }

    override fun serialize(dataFetcherResult: Any): String = runCatching {
        dataFetcherResult.toString()
    }.getOrElse {
        throw CoercingSerializeException("Data fetcher result $dataFetcherResult cannot be serialized to a String")
    }
}

/*
val graphqlUUIDType = GraphQLScalarType.newScalar()
    .name("UUID")
    .description("A type representing a formatted java.util.UUID")
    .coercing(UUIDCoercing)
    .build()

object UUIDCoercing : Coercing<UUID, String> {
    override fun parseValue(input: Any, graphQLContext: GraphQLContext, locale: Locale): UUID = runCatching {
        UUID.fromString(serialize(input, graphQLContext, locale))
    }.getOrElse {
        throw CoercingParseValueException("Expected valid UUID but was $input")
    }

    override fun parseLiteral(input: Value<*>, variables: CoercedVariables, graphQLContext: GraphQLContext, locale: Locale): UUID {
        val uuidString = (input as? StringValue)?.value
        return runCatching {
            UUID.fromString(uuidString)
        }.getOrElse {
            throw CoercingParseLiteralException("Expected valid UUID literal but was $uuidString")
        }
    }

    override fun serialize(dataFetcherResult: Any, graphQLContext: GraphQLContext, locale: Locale): String = runCatching {
        dataFetcherResult.toString()
    }.getOrElse {
        throw CoercingSerializeException("Data fetcher result $dataFetcherResult cannot be serialized to a String")
    }
}
 */