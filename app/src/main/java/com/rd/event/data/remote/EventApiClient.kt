package com.rd.event.data.remote

import com.rd.event.data.remote.dto.CheckInReturnDto
import com.rd.event.data.remote.dto.EventDto
import com.rd.event.domain.model.CheckIn
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface EventApiClient {
    @GET("events")
    suspend fun getEvents(): List<EventDto>

    @GET("events/{id}")
    suspend fun getEvent(@Path(value = "id") id: String): EventDto

    @POST("checkin")
    suspend fun doCheckIn(@Body payload: CheckIn): CheckInReturnDto
}
