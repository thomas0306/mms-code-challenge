package com.mms.oms.domain.mapper

import com.mms.oms.adapters.rest.model.Order
import com.mms.oms.domain.model.OrderStatus
import com.mms.oms.domain.model.PaymentStatus

typealias DomainOrder = com.mms.oms.domain.model.Order

object OrderMapper {

    fun toDomain(order: Order) = DomainOrder(
        tenant = order.tenant,
        status = OrderStatus.CREATED,
        paymentStatus = PaymentStatus.AWAIT_PAYMENT_INFO,
        createdAt = order.orderDate,
        cart = CartMapper.toDomainCart(order.cart, order.orderDate)
    )
}
