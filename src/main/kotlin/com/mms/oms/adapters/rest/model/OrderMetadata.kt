package com.mms.oms.adapters.rest.model

import java.util.UUID

data class OrderMetadata(
    val uuid: UUID = UUID.randomUUID()
)
