package com.rd.event.domain.use_case

import com.rd.event.common.Resource
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
class CheckInUseCaseTest {
    private lateinit var eventRepository: EventRepository
    private lateinit var checkInUseCase: CheckInUseCase

    @Before
    fun setup() {
        eventRepository = mockk()
        checkInUseCase = CheckInUseCase(eventRepository)
    }

    @Test
    fun `should call the use case and return success`() = runBlockingTest {
        coEvery { eventRepository.doCheckIn(getCheckIn()) } returns getCheckInResponse("200")

        val response = checkInUseCase(getCheckIn()).toList()

        Assert.assertTrue(response[0] is Resource.Loading)
        Assert.assertTrue(response[1] is Resource.Success)
        Assert.assertEquals(response[1].data, true)
    }

    @Test
    fun `should call the use case and return Exception`() = runBlockingTest {
        coEvery { eventRepository.doCheckIn(getCheckIn()) } returns getCheckInResponse("500")

        val response = checkInUseCase(getCheckIn()).toList()

        Assert.assertTrue(response[0] is Resource.Loading)
        Assert.assertTrue(response[1] is Resource.Error)
        Assert.assertEquals(
            response[1].message,
            "Erro ao realizar check in, tente novamente em instantes"
        )
    }

    @Test
    fun `should call the use case and return HttpException`() = runBlockingTest {

        coEvery { eventRepository.doCheckIn(getCheckIn()) }.throws(
            HttpException(
                Response.error<Any>(
                    409,
                    getCheckInResponse("409").toString().toResponseBody("plain/text".toMediaType())
                )
            )
        )
        val response = checkInUseCase(getCheckIn()).toList()

        Assert.assertTrue(response[0] is Resource.Loading)
        Assert.assertTrue(response[1] is Resource.Error)
        Assert.assertEquals(response[1].message, "HTTP 409 Response.error()")
    }

    @Test
    fun `should call the use case and return IOException`() = runBlockingTest {

        coEvery { eventRepository.doCheckIn(getCheckIn()) }.throws(
            IOException()
        )
        val response = checkInUseCase(getCheckIn()).toList()

        Assert.assertTrue(response[0] is Resource.Loading)
        Assert.assertTrue(response[1] is Resource.Error)
        Assert.assertEquals(response[1].message, "Não foi possível acessar o servidor. Verifique sua conexão com a internet.")
    }

    private fun getCheckIn(): CheckIn {
        return CheckIn("1", "lucas", "lucas@email.com")
    }

    private fun getCheckInResponse(code: String): CheckInReturn {
        return CheckInReturn(code)
    }
}