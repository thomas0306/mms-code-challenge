package com.mms.oms.domain.mapper

import com.mms.oms.adapters.rest.model.Item
import org.assertj.core.api.SoftAssertions
import org.junit.Test
import java.math.BigDecimal
import java.time.Instant

class ItemMapperTest {

    @Test
    fun `should map`() {
        // given
        val item = Item(
            itemId = "BK1250",
            quantity = 10,
            unitPrice = BigDecimal.valueOf(10.0)
        )

        // when
        val domainItem = ItemMapper.toDomainItem(item, Instant.now())

        // then
        SoftAssertions.assertSoftly {
            it.assertThat(domainItem.itemId).isEqualTo(item.itemId)
            it.assertThat(domainItem.quantity).isEqualTo(item.quantity)
            it.assertThat(domainItem.unitPrice).isEqualTo(item.unitPrice)
        }
    }
}
