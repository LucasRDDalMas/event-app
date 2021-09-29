package com.rd.event.di

import com.rd.event.common.Constants
import com.rd.event.data.remote.EventApiClient
import com.rd.event.data.repository.EventRepositoryImpl
import com.rd.event.domain.repository.EventRepository
import com.rd.event.domain.use_case.CheckInUseCase
import com.rd.event.domain.use_case.GetEventUseCase
import com.rd.event.domain.use_case.GetEventsUseCase
import com.rd.event.presentation.event_detail.EventDetailViewModel
import com.rd.event.presentation.event_list.EventListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory

val appModule = module {
    single { provideEventApiClient() }
    single { provideEventRepository(get()) }
    single { GetEventUseCase(get()) }
    single { GetEventsUseCase(get()) }
    single { CheckInUseCase(get()) }
    viewModel { EventListViewModel(get()) }
    viewModel { EventDetailViewModel(get(), get()) }
}

private fun provideEventApiClient(): EventApiClient {
    return Retrofit.Builder()
        .baseUrl(Constants.BASE_URL)
        .addConverterFactory(JacksonConverterFactory.create())
        .build()
        .create(EventApiClient::class.java)
}

private fun provideEventRepository(api: EventApiClient): EventRepository {
    return EventRepositoryImpl(api)
}