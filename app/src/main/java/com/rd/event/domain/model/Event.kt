package com.rd.event.domain.model

import java.time.ZonedDateTime

data class Event(
    val id: String,
    val title: String,
    val description: String,
    val date: String,
    val image: String,
    val latitude: Double,
    val longitude: Double,
    val price: String,
    val people: List<Any>
)
