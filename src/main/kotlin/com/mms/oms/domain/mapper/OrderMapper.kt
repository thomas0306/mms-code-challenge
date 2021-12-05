package com.mms.oms.domain.mapper

import com.mms.oms.adapters.repository.OrderRepository
import com.mms.oms.adapters.rest.model.Order
import com.mms.oms.domain.model.OrderStatus
import com.mms.oms.domain.model.PaymentStatus
import org.jetbrains.exposed.sql.ResultRow

typealias DomainOrder = com.mms.oms.domain.model.Order

object OrderMapper {

    fun toDomain(order: Order) = DomainOrder(
        tenant = order.tenant,
        status = OrderStatus.CREATED,
        paymentStatus = PaymentStatus.AWAIT_PAYMENT_INFO,
        createdAt = order.orderDate,
        cart = CartMapper.toDomain(order.cart, order.orderDate),
        customerData = CustomerDataMapper.toDomain(order.customerData, order.orderDate),
    )

    fun toDomain(resultRow: ResultRow) = DomainOrder(
        id = resultRow[OrderRepository.id].value,
        tenant = resultRow[OrderRepository.tenant],
        status = resultRow[OrderRepository.status],
        paymentStatus = resultRow[OrderRepository.paymentStatus],
        createdAt = resultRow[OrderRepository.createdAt],
        cart = null,
        customerData = null,
    )
}
