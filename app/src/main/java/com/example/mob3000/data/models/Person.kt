package com.example.mob3000.data.models

data class Person(
    val name: String,
    val age: String,
    val email: String,
    val testid: String,
    val userId: String,
    val documentId: String = "",
)
{
    constructor() : this ("", "", "", "", "", "")
}