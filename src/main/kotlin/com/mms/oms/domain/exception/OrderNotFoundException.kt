package com.mms.oms.domain.exception

import java.util.UUID

class OrderNotFoundException(orderId: UUID) : Throwable("Order [$orderId] not found")
