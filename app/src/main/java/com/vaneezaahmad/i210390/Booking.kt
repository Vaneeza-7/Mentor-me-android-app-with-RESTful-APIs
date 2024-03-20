package com.vaneezaahmad.i210390

data class Booking(
    val userId : String,
    val mentorName: String,
    val mentorPrice: String,
    val mentorImage: String,
    val date: String,
    val time: String
)
{
    constructor() : this("", "", "", "", "", "")
}
