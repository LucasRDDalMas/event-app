package com.rd.event.domain.use_case

import com.rd.event.common.Resource
import com.rd.event.data.remote.dto.EventDto
import com.rd.event.domain.model.CheckIn
import com.rd.event.domain.model.CheckInReturn
import com.rd.event.domain.repository.EventRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runBlockingTest
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

@ExperimentalCoroutinesApi
class GetEventsUseCaseTest {
    private lateinit var eventRepository: EventRepository
    private lateinit var getEventsUseCase: GetEventsUseCase

    @Before
    fun setup() {
        eventRepository = mockk()
        getEventsUseCase = GetEventsUseCase(eventRepository)
    }

    @Test
    fun `should call the use case and return success`() = runBlockingTest {
        val events = listEventDtoMock().map { it.toEvent() }
        coEvery { eventRepository.getEvents() } returns listEventDtoMock().map { it.toEvent() }

        val response = getEventsUseCase().toList()

        Assert.assertTrue(response[0] is Resource.Loading)
        Assert.assertTrue(response[1] is Resource.Success)
        Assert.assertEquals(response[1].data, events)
    }

    @Test
    fun `should call the use case and return HttpException`() = runBlockingTest {

        coEvery { eventRepository.getEvents() }.throws(
            HttpException(
                Response.error<Any>(
                    409,
                    "".toString().toResponseBody("plain/text".toMediaType())
                )
            )
        )
        val response = getEventsUseCase().toList()

        Assert.assertTrue(response[0] is Resource.Loading)
        Assert.assertTrue(response[1] is Resource.Error)
        Assert.assertEquals(response[1].message, "HTTP 409 Response.error()")
    }

    @Test
    fun `should call the use case and return IOException`() = runBlockingTest {

        coEvery { eventRepository.getEvents() }.throws(
            IOException()
        )
        val response = getEventsUseCase().toList()

        Assert.assertTrue(response[0] is Resource.Loading)
        Assert.assertTrue(response[1] is Resource.Error)
        Assert.assertEquals(response[1].message, "Não foi possível acessar o servidor. Verifique sua conexão com a internet.")
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