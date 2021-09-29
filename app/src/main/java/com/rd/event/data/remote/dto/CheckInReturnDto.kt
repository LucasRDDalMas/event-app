package com.rd.event.data.remote.dto


import com.fasterxml.jackson.annotation.JsonProperty
import com.rd.event.domain.model.CheckInReturn

data class CheckInReturnDto(
    @JsonProperty("code")
    val code: String
) {
    fun toCheckInReturn(): CheckInReturn {
        return CheckInReturn(code)
    }
}