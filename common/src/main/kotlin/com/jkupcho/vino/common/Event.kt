package com.jkupcho.vino.common

import com.fasterxml.jackson.annotation.JsonProperty
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

data class Event(@JsonProperty("type") @field:NotEmpty val type: String, @JsonProperty("payload") @field:NotNull val payload: Map<String, Any>)