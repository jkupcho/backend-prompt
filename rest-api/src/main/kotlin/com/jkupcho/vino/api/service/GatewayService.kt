package com.jkupcho.vino.api.service

interface GatewayService {

    fun isRateLimited(): Boolean

}