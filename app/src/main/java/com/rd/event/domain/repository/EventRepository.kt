package com.rd.event.domain.repository

import com.rd.event.domain.model.CheckIn
import com.rd.event.domain.model.CheckInReturn
import com.rd.event.domain.model.Event

interface EventRepository {
    suspend fun getEvents(): List<Event>
    suspend fun getEvent(id: String): Event
    suspend fun doCheckIn(payload: CheckIn): CheckInReturn
}