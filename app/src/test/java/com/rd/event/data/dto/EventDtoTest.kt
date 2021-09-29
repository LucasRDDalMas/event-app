package com.rd.event.data.dto

import com.rd.event.data.remote.dto.EventDto
import org.junit.Assert
import org.junit.Test

class EventDtoTest {

    @Test
    fun `Should instantiate a EventDto with success`() {
        val eventDto = EventDto(
            date = 1534784400000,
            description = "Description",
            id = "1",
            image = "http://image.com/pmpa.png",
            longitude = -51.2146267,
            latitude = -30.0392981,
            people = emptyList(),
            price = 10.99,
            title = "Title"
        )

        Assert.assertEquals(eventDto.date, 1534784400000)
        Assert.assertEquals(eventDto.description, "Description")
        Assert.assertEquals(eventDto.id, "1")
        Assert.assertEquals(eventDto.image, "http://image.com/pmpa.png")
        Assert.assertEquals(eventDto.longitude, -51.2146267, 7.toDouble())
        Assert.assertEquals(eventDto.latitude, -30.0392981, 7.toDouble())
        Assert.assertEquals(eventDto.price, 10.99, 7.toDouble())
        Assert.assertEquals(eventDto.title, "Title")
    }

    @Test
    fun `Should call toEvent and convert a EventDto to Event with success`() {
        val eventDto = EventDto(
            date = 1534784400000,
            description = "Description",
            id = "1",
            image = "http://image.com/pmpa.png",
            longitude = -51.2146267,
            latitude = -30.0392981,
            people = emptyList(),
            price = 10.99,
            title = "Title"
        )

        val event = eventDto.toEvent()

        Assert.assertEquals(event.date, "20/08/2018 14:00")
        Assert.assertEquals(event.description, "Description")
        Assert.assertEquals(event.id, "1")
        Assert.assertEquals(event.image, "https://image.com/pmpa.png")
        Assert.assertEquals(event.longitude, -51.2146267, 7.toDouble())
        Assert.assertEquals(event.latitude, -30.0392981, 7.toDouble())
        Assert.assertEquals(event.price, "10.99")
        Assert.assertEquals(event.title, "Title")
    }
}