package com.rd.event.domain.use_case

import com.rd.event.common.Resource
import com.rd.event.data.remote.dto.EventDto
import com.rd.event.domain.model.CheckIn
import com.rd.event.domain.model.CheckInReturn
import com.rd.event.domain.model.Event
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
class GetEventUseCaseTest {
    private lateinit var eventRepository: EventRepository
    private lateinit var getEventUseCase: GetEventUseCase

    @Before
    fun setup() {
        eventRepository = mockk()
        getEventUseCase = GetEventUseCase(eventRepository)
    }

    @Test
    fun `should call the use case and return success`() = runBlockingTest {
        val event = listEventDtoMock()
        coEvery { eventRepository.getEvent("1") } returns event

        val response = getEventUseCase("1").toList()

        Assert.assertTrue(response[0] is Resource.Loading)
        Assert.assertTrue(response[1] is Resource.Success)
        Assert.assertEquals(response[1].data, event)
    }

    @Test
    fun `should call the use case and return HttpException`() = runBlockingTest {

        coEvery { eventRepository.getEvent("1") }.throws(
            HttpException(
                Response.error<Any>(
                    409,
                    "".toString().toResponseBody("plain/text".toMediaType())
                )
            )
        )
        val response = getEventUseCase("1").toList()

        Assert.assertTrue(response[0] is Resource.Loading)
        Assert.assertTrue(response[1] is Resource.Error)
        Assert.assertEquals(response[1].message, "HTTP 409 Response.error()")
    }

    @Test
    fun `should call the use case and return IOException`() = runBlockingTest {

        coEvery { eventRepository.getEvent("1") }.throws(
            IOException()
        )
        val response = getEventUseCase("1").toList()

        Assert.assertTrue(response[0] is Resource.Loading)
        Assert.assertTrue(response[1] is Resource.Error)
        Assert.assertEquals(response[1].message, "Não foi possível acessar o servidor. Verifique sua conexão com a internet.")
    }

    private fun listEventDtoMock(): Event {
        return EventDto(
                date = 1534784400000,
                description = "Description",
                id = "1",
                image = "http://image.com/pmpa.png",
                longitude = -51.2146267,
                latitude = -30.0392981,
                people = emptyList(),
                price = 10.99,
                title = "Title"
            ).toEvent()
    }
}