package com.vaneezaahmad.i210390

data class Message(
    val message: String,
    val sender: String,
    val receiver: String,
    val timestamp: Long,
    val read : Boolean,
    val receiverImage : String
)
{
    constructor() : this("", "", "", -1, false, "")
}
