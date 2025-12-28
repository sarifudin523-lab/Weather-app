package com.example.weatherdemo.data

data class Message(
    var id: String = "",
    var username: String = "",
    var message: String = "",
    val time: String = "",
    var timestamp: Long = 0L
)
