package com.rd.event.presentation.event_list

import androidx.lifecycle.*
import com.rd.event.common.Resource
import com.rd.event.domain.model.Event
import com.rd.event.domain.use_case.GetEventsUseCase
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class EventListViewModel(private val getEventsUseCase: GetEventsUseCase): ViewModel() {
    private var _events: MutableLiveData<List<Event>> = MutableLiveData<List<Event>>()
    val events: LiveData<List<Event>>
        get() = _events

    private var _loading: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean>
        get() = _loading

    private var _error: MutableLiveData<String> = MutableLiveData<String>()
    val error: LiveData<String>
        get() = _error

    init {
        viewModelScope.launch { loadEvents() }
    }

    private fun loadEvents() {
        getEventsUseCase().onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _loading.value = false
                    _events.value = result.data ?: emptyList()
                }
                is Resource.Error -> {
                    _loading.value = false
                    _error.value = result.message ?: "Unexpected error"
                }
                is Resource.Loading -> {
                    _loading.value = true
                }
            }
        }.launchIn(viewModelScope)
    }
}
