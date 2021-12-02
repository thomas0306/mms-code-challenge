package com.mms.oms.adapters.rest

import com.mms.oms.adapters.rest.model.Order
import com.mms.oms.domain.mapper.OrderMapper
import com.mms.oms.domain.service.OrderService
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.application.log
import io.ktor.http.HttpStatusCode
import io.ktor.locations.Locations
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.post
import io.ktor.routing.routing

fun Application.configureOrderRouting() {

    install(Locations) {
    }

    routing {
        post("/order") {
            val order = call.receive<Order>()
            this@configureOrderRouting.log.info("Creating order [$order]")
            val domainOrder = OrderMapper.toDomainOrder(order)
            OrderService().createOrder(domainOrder)
            call.respond(HttpStatusCode.OK, domainOrder)
        }
    }
}