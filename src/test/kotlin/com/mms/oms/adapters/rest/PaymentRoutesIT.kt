package com.mms.oms.adapters.rest

import com.mms.oms.adapters.rest.model.EventCode
import com.mms.oms.adapters.rest.model.Payment
import com.mms.oms.support.IntegrationTestBase
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.setBody
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.assertj.core.api.Assertions.assertThat
import java.math.BigDecimal
import java.time.Instant
import kotlin.test.Test

class PaymentRoutesIT : IntegrationTestBase() {

    @Test
    fun `should accepts payment`() {
        with(engine) {
            handleRequest(HttpMethod.Post, PAYMENT_ENDPOINT) {
                addHeader(HttpHeaders.ContentType, "application/json")
                setBody(Json.encodeToString(completePayment()))
            }.apply {
                assertThat(response.status()).isEqualTo(HttpStatusCode.Accepted)
            }
        }
    }

    @Test
    fun `should not process if request body in-complete`() {
        with(engine) {
            handleRequest(HttpMethod.Post, PAYMENT_ENDPOINT) {
                addHeader(HttpHeaders.ContentType, "application/json")
                setBody("{}")
            }.apply {
                assertThat(response.status()).isEqualTo(HttpStatusCode.BadRequest)
            }
        }
    }

    private fun completePayment() = Payment(
        transactionId = "transaction-id",
        merchantReference = "327faae9-5d2f-463e-81f0-7cf0ce29e7cd",
        amount = BigDecimal.TEN,
        currency = "EUR",
        eventCode = EventCode.AUTHORISATION,
        pspReference = "psp-reference",
        createdAt = Instant.now(),
    )
}
