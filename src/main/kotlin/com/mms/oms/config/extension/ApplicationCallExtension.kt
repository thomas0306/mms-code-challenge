package com.mms.oms.config.extension

import io.ktor.application.ApplicationCall
import io.ktor.features.BadRequestException
import io.ktor.request.receive
import io.ktor.util.error
import org.valiktor.ConstraintViolationException
import kotlin.reflect.typeOf

suspend inline fun <reified T : Any> ApplicationCall.receiveOrBadRequestException(): T =
    try {
        receive(typeOf<T>())
    } catch (e: Exception) {
        application.environment.log.error(e)
        if (e is ConstraintViolationException) {
            throw e
        }
        throw BadRequestException("Cannot deserialize request", e)
    }
