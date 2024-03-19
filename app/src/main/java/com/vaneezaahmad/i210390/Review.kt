package com.vaneezaahmad.i210390

data class Review(
    val mentorName: String,
    val rating: Float,
    val reviewText: String
)
{
    constructor() : this("", 0.0F, "")
}
