package com.mms.oms.domain.service

import com.mms.oms.domain.model.Order

interface OrderService {
    suspend fun createOrder(order: Order)
}

