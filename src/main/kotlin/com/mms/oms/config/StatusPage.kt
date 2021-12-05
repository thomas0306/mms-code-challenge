package com.mms.oms.config

import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.BadRequestException
import io.ktor.features.StatusPages
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.response.respondText
import org.valiktor.ConstraintViolationException
import org.valiktor.i18n.mapToMessage
import java.util.Locale

fun Application.configureStatusPage() {
    install(StatusPages) {
        exception<BadRequestException> { cause ->
            call.respondText(cause.cause?.message ?: "", status = HttpStatusCode.BadRequest)
        }

        exception<ConstraintViolationException> { cause ->
            call.respond(
                HttpStatusCode.BadRequest,
                cause
                    .constraintViolations
                    .mapToMessage(baseName = "messages", locale = Locale.ENGLISH)
                    .associate { it.property to it.message }
            )
        }
    }
}
