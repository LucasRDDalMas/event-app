package com.rd.event.data.remote.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.rd.event.domain.model.Event
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.SimpleDateFormat
import java.util.*

data class EventDto(
    @JsonProperty("date")
    val date: Long,
    @JsonProperty("description")
    val description: String,
    @JsonProperty("id")
    val id: String,
    @JsonProperty("image")
    val image: String,
    @JsonProperty("latitude")
    val latitude: Double,
    @JsonProperty("longitude")
    val longitude: Double,
    @JsonProperty("people")
    val people: List<Any>,
    @JsonProperty("price")
    val price: Double,
    @JsonProperty("title")
    val title: String
) {
    fun toEvent(): Event {
        return Event(
            id = id,
            title = title,
            description = description,
            date = getDate(),
            image = getImageUrl(),
            latitude = latitude,
            longitude = longitude,
            price = getDecimalPrice(),
            people = people
        )
    }

    private fun getDate(): String {
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale("pt","BR"))
        return sdf.format(Date(date).time)
    }

    private fun getDecimalPrice(): String {
        return BigDecimal(price).setScale(2, RoundingMode.HALF_EVEN).toString()
    }

    private fun getImageUrl(): String  {
        return image.replace("http:", "https:")
    }
}