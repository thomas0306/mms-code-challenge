package com.mms.oms.domain.mapper

import com.mms.oms.adapters.rest.model.Order
import com.mms.oms.domain.model.Status

typealias DomainOrder = com.mms.oms.domain.model.Order

object OrderMapper {

    fun toDomainOrder(order: Order) = DomainOrder(
        tenant = order.tenant,
        status = Status.CREATED,
        createdAt = order.orderDate,
        cart = CartMapper.toDomainCart(order.cart, order.orderDate)
    )
}
