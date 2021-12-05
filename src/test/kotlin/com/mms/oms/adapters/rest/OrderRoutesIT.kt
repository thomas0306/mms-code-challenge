package com.mms.oms.adapters.rest

import com.mms.oms.adapters.rest.model.Address
import com.mms.oms.adapters.rest.model.AddressRole.BILLING
import com.mms.oms.adapters.rest.model.AddressRole.DELIVERY
import com.mms.oms.adapters.rest.model.Cart
import com.mms.oms.adapters.rest.model.CustomerData
import com.mms.oms.adapters.rest.model.Item
import com.mms.oms.adapters.rest.model.Order
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

class OrderRoutesIT : IntegrationTestBase() {

    @Test
    fun `should accepts order`() {
        with(engine) {
            handleRequest(HttpMethod.Post, ORDER_ENDPOINT) {
                addHeader(HttpHeaders.ContentType, "application/json")
                setBody(Json.encodeToString(completeOrder()))
            }.apply {
                assertThat(response.status()).isEqualTo(HttpStatusCode.Accepted)
            }
        }
    }

    @Test
    fun `should not process if request body in-complete`() {
        with(engine) {
            handleRequest(HttpMethod.Post, ORDER_ENDPOINT) {
                addHeader(HttpHeaders.ContentType, "application/json")
                setBody("{}")
            }.apply {
                assertThat(response.status()).isEqualTo(HttpStatusCode.BadRequest)
            }
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
                    role = setOf(DELIVERY, BILLING),
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
}
