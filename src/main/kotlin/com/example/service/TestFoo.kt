package com.example.service

import graphql.schema.DataFetchingEnvironment

data class TestFoo(val name: String, val age: Int) {
    fun getTime(dataFetchingEnvironment: DataFetchingEnvironment): String {
        return "Time is 12:00"
    }
}
