package com.rd.event.data.repository

import com.rd.event.data.remote.EventApiClient
import com.rd.event.data.remote.dto.CheckInReturnDto
import com.rd.event.data.remote.dto.EventDto
import com.rd.event.domain.model.CheckIn
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class EventRepositoryImplTest {

    private lateinit var eventApiClient: EventApiClient
    private lateinit var eventRepositoryImpl: EventRepositoryImpl

    @Before
    fun setup() {
        eventApiClient = mockk()
        eventRepositoryImpl = EventRepositoryImpl(eventApiClient)
    }

    @Test
    fun `should call getEvents and return a list of Event`() = runBlockingTest {
        coEvery { eventApiClient.getEvents() } returns listEventDtoMock()

        val events = eventRepositoryImpl.getEvents()

        Assert.assertTrue(events.size == 2)
        Assert.assertEquals(events.first().date, "20/08/2018 14:00")
        Assert.assertEquals(events.first().description, "Description")
        Assert.assertEquals(events.first().id, "1")
        Assert.assertEquals(events.first().image, "https://image.com/pmpa.png")
        Assert.assertEquals(events.first().longitude, -51.2146267, 7.toDouble())
        Assert.assertEquals(events.first().latitude, -30.0392981, 7.toDouble())
        Assert.assertEquals(events.first().price, "10.99")
        Assert.assertEquals(events.first().title, "Title")
    }

    @Test
    fun `should call getEvents and return a empty list`() = runBlockingTest {
        coEvery { eventApiClient.getEvents() } returns emptyList()

        val events = eventRepositoryImpl.getEvents()

        Assert.assertTrue(events.isEmpty())
    }

    @Test
    fun `should call getEvent and return a Event`() = runBlockingTest {
        val id = "1"
        coEvery { eventApiClient.getEvent(id) } returns listEventDtoMock().first()

        val event = eventRepositoryImpl.getEvent(id)

        Assert.assertEquals(event.date, "20/08/2018 14:00")
        Assert.assertEquals(event.description, "Description")
        Assert.assertEquals(event.id, id)
        Assert.assertEquals(event.image, "https://image.com/pmpa.png")
        Assert.assertEquals(event.longitude, -51.2146267, 7.toDouble())
        Assert.assertEquals(event.latitude, -30.0392981, 7.toDouble())
        Assert.assertEquals(event.price, "10.99")
        Assert.assertEquals(event.title, "Title")
    }

    @Test
    fun `should call doCheckIn and finish with success`() = runBlockingTest {
        val payload = CheckIn("1", "lucas", "lucas@email.com")

        coEvery { eventApiClient.doCheckIn(payload) } returns CheckInReturnDto("200")
        val response = eventRepositoryImpl.doCheckIn(payload)

        coVerify(exactly = 1) { eventApiClient.doCheckIn(payload) }
        Assert.assertEquals(response.code, "200")
    }

    private fun listEventDtoMock(): List<EventDto> {
        return listOf(
            EventDto(
                date = 1534784400000,
                description = "Description",
                id = "1",
                image = "http://image.com/pmpa.png",
                longitude = -51.2146267,
                latitude = -30.0392981,
                people = emptyList(),
                price = 10.99,
                title = "Title"
            ),
            EventDto(
                date = 1534784400000,
                description = "Description",
                id = "2",
                image = "http://image.com/pmpa.png",
                longitude = -51.2146267,
                latitude = -30.0392981,
                people = emptyList(),
                price = 10.99,
                title = "Title 2"
            )
        )
    }
}