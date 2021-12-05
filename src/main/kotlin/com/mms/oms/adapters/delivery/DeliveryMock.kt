package com.mms.oms.adapters.delivery

import com.mms.oms.adapters.kafka.ShipmentProducerImpl
import com.mms.oms.adapters.repository.ShipmentRepository
import com.mms.oms.config.cron.Scheduled
import com.mms.oms.domain.mapper.ShipmentMapper
import com.mms.oms.domain.model.ShipmentStatus
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.quartz.Job
import org.quartz.JobExecutionContext
import org.slf4j.LoggerFactory
import java.time.Instant

@Scheduled(cron = "0/10 * * ? * * *", name = "delivery-mock")
class DeliveryMock : Job, KoinComponent {
    private val logger = LoggerFactory.getLogger(javaClass)

    private val shipmentProducer: ShipmentProducerImpl by inject()

    private val endStatus = listOf(ShipmentStatus.DELIVERED, ShipmentStatus.DELIVERY_FAILED)

    override fun execute(context: JobExecutionContext?) = transaction {
        logger.info("Delivery Mock wakes")

        ShipmentRepository.select {
            ShipmentRepository.status eq ShipmentStatus.NOTIFIED
        }.forEach {
            val shipment = ShipmentMapper.toDomain(it)
            val shipmentUpdate = shipment.copy(
                status = getRandomEndStatus(),
                actualDeliveryAt = Instant.now(),
            )
            logger.info(
                "Updating shipment [$shipment.id] for order [${shipment.orderId}] " +
                    "with status [${shipment.status} -> ${shipmentUpdate.status}]"
            )

            shipmentProducer.produce(shipmentUpdate)
        }
    }

    private fun getRandomEndStatus(): ShipmentStatus {
        return endStatus.asSequence().shuffled().first()
    }
}
