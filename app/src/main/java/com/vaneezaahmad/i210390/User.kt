package com.vaneezaahmad.i210390

data class User(
    val name : String,
    val email: String,
    val phone : String,
    val city : String,
    val country : String,
    val image : String
)
{
    constructor() : this("","","","","","")
}
