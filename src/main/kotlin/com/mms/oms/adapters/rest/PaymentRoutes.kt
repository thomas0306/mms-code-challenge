package com.mms.oms.adapters.rest

import com.mms.oms.adapters.rest.model.Payment
import com.mms.oms.config.extension.receiveOrBadRequestException
import com.mms.oms.domain.mapper.PaymentMapper
import com.mms.oms.domain.service.PaymentService
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.log
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.routing.post
import io.ktor.routing.routing
import org.koin.ktor.ext.inject

private const val PAYMENT_ENDPOINT = "/payment"

fun Application.configurePaymentRouting() {

    val paymentService: PaymentService by inject()

    routing {
        post(PAYMENT_ENDPOINT) {
            val payment = call.receiveOrBadRequestException<Payment>()
            this@configurePaymentRouting.log.info("Received payment event [$payment]")
            val domainPayment = PaymentMapper.toDomain(payment)
            paymentService.submitPayment(domainPayment)
            call.respond(HttpStatusCode.Accepted, PaymentMapper.toApi(domainPayment))
        }
    }
}

