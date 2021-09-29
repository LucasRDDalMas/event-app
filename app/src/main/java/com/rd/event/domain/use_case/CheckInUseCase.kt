package com.rd.event.domain.use_case

import com.rd.event.common.Resource
import com.rd.event.domain.exception.InvalidCheckInException
import com.rd.event.domain.model.CheckIn
import com.rd.event.domain.repository.EventRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import java.lang.Exception

class CheckInUseCase(private val eventRepository: EventRepository) {
    operator fun invoke(payload: CheckIn): Flow<Resource<Boolean>> = flow {
        try {
            emit(Resource.Loading())
            val response = eventRepository.doCheckIn(payload)

            if (response.code != "200") {
                throw InvalidCheckInException("Erro ao realizar check in, tente novamente em instantes")
            }
            emit(Resource.Success(true))
        } catch(e: HttpException) {
            emit(Resource.Error(e.localizedMessage ?: "Um erro inesperado ocorreu!"))
        } catch(e: IOException) {
            emit(Resource.Error("Não foi possível acessar o servidor. Verifique sua conexão com a internet."))
        } catch(e: Exception) {
            emit(Resource.Error(e.message ?: "Um erro inesperado ocorreu!"))
        }
    }
}