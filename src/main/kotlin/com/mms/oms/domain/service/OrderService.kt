package com.mms.oms.domain.service

import com.mms.oms.domain.model.Order

interface OrderService {
    suspend fun submitOrder(order: Order)
    suspend fun persistOrder(order: Order)
    suspend fun updateOrder(order: Order)
}
