package com.rd.event.data.repository

import com.rd.event.data.remote.EventApiClient
import com.rd.event.domain.model.CheckIn
import com.rd.event.domain.model.CheckInReturn
import com.rd.event.domain.model.Event
import com.rd.event.domain.repository.EventRepository

class EventRepositoryImpl(private val eventApiClient: EventApiClient) : EventRepository {
    override suspend fun getEvents(): List<Event> {
        return eventApiClient.getEvents().map {
            it.toEvent()
        }
    }

    override suspend fun getEvent(id: String): Event {
        return eventApiClient.getEvent(id).toEvent()
    }

    override suspend fun doCheckIn(payload: CheckIn): CheckInReturn {
        return eventApiClient.doCheckIn(payload).toCheckInReturn()
    }
}