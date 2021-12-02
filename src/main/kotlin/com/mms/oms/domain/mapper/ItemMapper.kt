package com.mms.oms.domain.mapper

import com.mms.oms.adapters.rest.model.Item
import java.time.Instant

typealias DomainItem = com.mms.oms.domain.model.Item

object ItemMapper {
    fun toDomainItem(item: Item, createdAt: Instant) = DomainItem(
        itemId = item.itemId,
        unitPrice = item.unitPrice,
        quantity = item.quantity,
        createdAt = createdAt
    )
}
