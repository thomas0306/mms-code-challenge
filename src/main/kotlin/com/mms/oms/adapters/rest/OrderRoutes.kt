package com.mms.oms.adapters.rest

import com.mms.oms.adapters.rest.model.Order
import com.mms.oms.domain.mapper.OrderMapper
import com.mms.oms.domain.service.OrderService
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.log
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.header
import io.ktor.response.respond
import io.ktor.routing.post
import io.ktor.routing.routing
import org.koin.ktor.ext.inject

private val ORDER_ENDPOINT = "/order"

fun Application.configureOrderRouting() {

    val orderService: OrderService by inject()

    routing {
        post(ORDER_ENDPOINT) {
            val order = call.receive<Order>()
            this@configureOrderRouting.log.info("Creating order [$order]")
            val domainOrder = OrderMapper.toDomain(order)
            orderService.submitOrder(domainOrder)
            call.response.header(HttpHeaders.Location, "$ORDER_ENDPOINT/${domainOrder.id}")
            call.respond(HttpStatusCode.Accepted, domainOrder)
        }
    }
}
