package com.rd.event.domain.use_case

import com.rd.event.common.Resource
import com.rd.event.domain.model.Event
import com.rd.event.domain.repository.EventRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

class GetEventUseCase(private val eventRepository: EventRepository) {
    operator fun invoke(eventId: String): Flow<Resource<Event>> = flow {
        try {
            emit(Resource.Loading())
            val events = eventRepository.getEvent(eventId)
            emit(Resource.Success(events))
        } catch(e: HttpException) {
            emit(Resource.Error(e.localizedMessage ?: "Um erro inesperado ocorreu!"))
        } catch(e: IOException) {
            emit(Resource.Error("Não foi possível acessar o servidor. Verifique sua conexão com a internet."))
        }
    }
}