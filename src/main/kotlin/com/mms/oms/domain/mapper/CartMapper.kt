package com.mms.oms.domain.mapper

import com.mms.oms.adapters.rest.model.Cart
import java.time.Instant

typealias DomainCart = com.mms.oms.domain.model.Cart

object CartMapper {
    fun toDomain(cart: Cart, createdAt: Instant) = DomainCart(
        currency = cart.currency,
        totalPrice = cart.totalPrice,
        discountedPrice = cart.discountedPrice,
        shippingPrice = cart.shippingPrice,
        items = cart.items.map { ItemMapper.toDomain(it, createdAt) },
        createdAt = createdAt
    )
}
