package com.mms.oms.e2e

import com.mms.oms.adapters.rest.ORDER_ENDPOINT
import com.mms.oms.adapters.rest.PAYMENT_ENDPOINT
import com.mms.oms.adapters.rest.model.Address
import com.mms.oms.adapters.rest.model.AddressRole
import com.mms.oms.adapters.rest.model.Cart
import com.mms.oms.adapters.rest.model.CustomerData
import com.mms.oms.adapters.rest.model.EventCode
import com.mms.oms.adapters.rest.model.Item
import com.mms.oms.adapters.rest.model.Order
import com.mms.oms.adapters.rest.model.Payment
import com.mms.oms.domain.mapper.DomainOrder
import com.mms.oms.support.IntegrationTestBase
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.setBody
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.math.BigDecimal
import java.time.Instant
import java.util.UUID

class OrderProcessingE2E : IntegrationTestBase() {

    @Test
    fun `should ships order`() = runBlocking {
        with(engine) {
            val (orderId, totalPrice) = handleRequest(HttpMethod.Post, ORDER_ENDPOINT) {
                addHeader(HttpHeaders.ContentType, "application/json")
                setBody(Json.encodeToString(completeOrder()))
            }.apply {
                assertThat(response.status()).isEqualTo(HttpStatusCode.Accepted)
            }.run {
                val order = Json.decodeFromString<DomainOrder>(response.content!!)
                order.id to order.cart!!.totalPrice
            }

            delay(2000)

            handleRequest(HttpMethod.Post, PAYMENT_ENDPOINT) {
                addHeader(HttpHeaders.ContentType, "application/json")
                setBody(Json.encodeToString(completePaymentWith(orderId, totalPrice)))
            }.apply {
                assertThat(response.status()).isEqualTo(HttpStatusCode.Accepted)
            }

            delay(2000)
        }
    }

    private fun completeOrder() = Order(
        orderDate = Instant.now(),
        tenant = "DE",
        customerData = CustomerData(
            firstName = "FirstName",
            lastName = "LastName",
            phoneNumber = "+49123123123",
            emailAddress = "FirstName.LastName@domain.com",
            addresses = listOf(
                Address(
                    role = listOf(AddressRole.DELIVERY, AddressRole.BILLING),
                    street = "Street",
                    streetNumber = "1",
                    zipCode = "81827",
                    city = "Munich",
                    country = "Germany",
                ),
            ),
        ),
        cart = Cart(
            currency = "EUR",
            totalPrice = BigDecimal.valueOf(200.00),
            discountedPrice = BigDecimal.valueOf(180.00),
            shippingPrice = BigDecimal.valueOf(20.00),
            items = listOf(
                Item(
                    itemId = "BK1250",
                    unitPrice = BigDecimal.valueOf(20.00),
                    quantity = 10
                ),
            ),
        ),
    )

    private fun completePaymentWith(orderId: UUID, totalPrice: BigDecimal) = Payment(
        transactionId = "transaction-id",
        merchantReference = orderId.toString(),
        amount = totalPrice,
        currency = "EUR",
        eventCode = EventCode.AUTHORISATION,
        pspReference = "psp-reference",
        createdAt = Instant.now(),
    )
}
