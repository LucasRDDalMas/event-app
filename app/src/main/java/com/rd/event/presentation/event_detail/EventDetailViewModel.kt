package com.rd.event.presentation.event_detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rd.event.common.Resource
import com.rd.event.domain.model.CheckIn
import com.rd.event.domain.model.Event
import com.rd.event.domain.use_case.CheckInUseCase
import com.rd.event.domain.use_case.GetEventUseCase
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class EventDetailViewModel(private val getEventUseCase: GetEventUseCase, private val checkInUseCase: CheckInUseCase): ViewModel() {
    private var _event: MutableLiveData<Event> = MutableLiveData<Event>()
    val event: LiveData<Event>
        get() = _event

    private var _loading: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean>
        get() = _loading

    private var _alertDialog: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val alertDialog: LiveData<Boolean>
        get() = _alertDialog

    private var _error: MutableLiveData<String> = MutableLiveData<String>()
    val error: LiveData<String>
        get() = _error

    fun loadActivityEvent(id: String) {
        viewModelScope.launch { loadEvent(id) }
    }

    fun doCheckIn(id: String, userName: String, userEmail: String) {
        viewModelScope.launch { checkIn(id, userName, userEmail) }
    }

    private fun checkIn(id: String, userName: String, userEmail: String) {
        checkInUseCase(CheckIn(id, userName, userEmail)).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _alertDialog.value = false
                    _loading.value = false
                }
                is Resource.Error -> {
                    _loading.value = false
                }
                is Resource.Loading -> {
                    _loading.value = true
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun loadEvent(id: String) {
        getEventUseCase(id).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _event.value = result.data
                    _loading.value = false
                }
                is Resource.Error -> {
                    _loading.value = false
                }
                is Resource.Loading -> {
                    _loading.value = true
                }
            }
        }.launchIn(viewModelScope)
    }
}
