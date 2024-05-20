package com.vaneezaahmad.i210390

data class Message(
    var message: String,
    val sender: String,
    val receiver: String,
    var timestamp: Long,
    var status : String,
    var receiverImage : String,
    var senderImage : String,
    val key : String? = null,
    val audioUrl: String? = null,
    val mediaUrl: String? = null,
    val type: String = "text"
)
{
    constructor() : this("", "", "", -1, "", "", "", null, null, null, "text")
}
