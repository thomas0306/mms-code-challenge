package com.mms.oms.domain.service

import com.mms.oms.adapters.kafka.OrderProducerImpl
import com.mms.oms.adapters.repository.AddressRepository
import com.mms.oms.adapters.repository.CartRepository
import com.mms.oms.adapters.repository.CustomerDataRepository
import com.mms.oms.adapters.repository.ItemRepository
import com.mms.oms.adapters.repository.OrderCustomerDataRepository
import com.mms.oms.adapters.repository.OrderRepository
import com.mms.oms.domain.exception.OrderNotFoundException
import com.mms.oms.domain.mapper.OrderMapper
import com.mms.oms.domain.model.Cart
import com.mms.oms.domain.model.CustomerData
import com.mms.oms.domain.model.Order
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.update
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.time.Instant
import java.util.UUID

class OrderServiceImpl : OrderService, KoinComponent {

    private val orderProducer: OrderProducerImpl by inject()
    private val paymentService: PaymentService by inject()

    override suspend fun submitOrder(order: Order) {
        orderProducer.produce(order)
    }

    override suspend fun getOrder(orderId: UUID): Order =
        OrderRepository
            .select {
                OrderRepository.id eq orderId
            }.singleOrNull()?.let {
                OrderMapper.toDomain(it)
            } ?: throw OrderNotFoundException(orderId)

    override suspend fun persistOrder(order: Order) = newSuspendedTransaction {
        OrderRepository.insert {
            it[id] = order.id
            it[tenant] = order.tenant
            it[status] = order.status
            it[paymentStatus] = order.paymentStatus
            it[createdAt] = order.createdAt
            it[updatedAt] = order.updatedAt
        }

        order.cart?.let { insertCartData(order, it) }
        order.customerData?.let { insertCustomerData(order, it) }

        paymentService.maybeConcludesPayment(order.id)

        return@newSuspendedTransaction
    }

    private fun insertCartData(order: Order, cart: Cart) {
        order.cart?.also { c ->
            CartRepository.insert {
                it[id] = c.id
                it[currency] = c.currency
                it[totalPrice] = c.totalPrice.toFloat()
                it[discountedPrice] = c.discountedPrice.toFloat()
                it[shippingPrice] = c.shippingPrice.toFloat()
                it[orderId] = order.id
                it[createdAt] = c.createdAt
                it[updatedAt] = c.updatedAt
            }
        }

        order.cart?.items?.forEach { i ->
            ItemRepository.insert {
                it[id] = i.id
                it[itemId] = i.itemId
                it[unitPrice] = i.unitPrice.toFloat()
                it[quantity] = i.quantity
                it[createdAt] = i.createdAt
                it[updatedAt] = i.updatedAt
                it[cartId] = cart.id
            }
        }
    }

    private fun insertCustomerData(
        order: Order,
        customerData: CustomerData
    ) {
        order.customerData?.also { cd ->
            CustomerDataRepository.insert {
                it[id] = cd.id
                it[firstName] = cd.firstName
                it[lastName] = cd.lastName
                it[phoneNumber] = cd.phoneNumber
                it[emailAddress] = cd.emailAddress
                it[createdAt] = cd.createdAt
            }

            OrderCustomerDataRepository.insert {
                it[id] = UUID.randomUUID()
                it[orderId] = order.id
                it[customerDataId] = cd.id
                it[createdAt] = cd.createdAt
            }
        }

        order.customerData?.addresses?.forEach { a ->
            AddressRepository.insert { it ->
                it[id] = a.id
                it[roles] = Array(a.roles.size) { idx ->
                    a.roles.toList()[idx]
                }
                it[street] = a.street
                it[streetNumber] = a.streetNumber
                it[zipCode] = a.zipCode
                it[city] = a.city
                it[country] = a.country
                it[createdAt] = a.createdAt
                it[customerDataId] = customerData.id
            }
        }
    }

    override suspend fun updateOrder(
        order: Order
    ) = newSuspendedTransaction {
        OrderRepository.update({ OrderRepository.id eq order.id }) {
            order.status.apply {
                it[status] = this
            }

            order.paymentStatus.apply {
                it[paymentStatus] = this
            }

            it[updatedAt] = Instant.now()
        }

        return@newSuspendedTransaction
    }
}
