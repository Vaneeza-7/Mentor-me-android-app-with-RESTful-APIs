package com.vaneezaahmad.i210390

data class Mentor(
    val name: String,
    val price: String,
    val role: String,
    val status: String,
    val image: String,
    val category: String,
    val description: String,
    val email: String,
    val video : String
)
{
    constructor() : this("", "", "", "", "", "", "", "", "")
}