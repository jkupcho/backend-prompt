package com.jkupcho.vino.common

import com.fasterxml.jackson.annotation.JsonProperty

data class Event(@JsonProperty("type") val type: String, @JsonProperty("payload") val payload: Map<String, Any>)