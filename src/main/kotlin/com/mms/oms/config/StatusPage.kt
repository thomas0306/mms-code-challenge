package com.mms.oms.config

import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.BadRequestException
import io.ktor.features.StatusPages
import io.ktor.http.HttpStatusCode
import io.ktor.response.respondText

fun Application.configureStatusPage() {
    install(StatusPages) {
        exception<BadRequestException> { cause ->
            call.respondText(cause.cause?.message ?: "", status = HttpStatusCode.BadRequest)
        }
    }
}
