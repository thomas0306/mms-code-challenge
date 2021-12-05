package com.mms.oms.domain.mapper

import com.mms.oms.adapters.rest.model.Cart
import com.mms.oms.adapters.rest.model.Item
import org.assertj.core.api.SoftAssertions
import org.junit.Test
import java.math.BigDecimal
import java.time.Instant

class CartMapperTest {

    @Test
    fun `should map`() {
        // given
        val item = Item(
            itemId = "BK1250",
            quantity = 10,
            unitPrice = BigDecimal.valueOf(10.0)
        )

        val cart = Cart(
            currency = "EUR",
            totalPrice = BigDecimal.valueOf(200.0),
            discountedPrice = BigDecimal.valueOf(180.0),
            shippingPrice = BigDecimal.valueOf(20.0),
            items = listOf(item)
        )

        // when
        val domainCart = CartMapper.toDomain(cart, Instant.now())

        // then
        SoftAssertions.assertSoftly {
            it.assertThat(domainCart.currency).isEqualTo(cart.currency)
            it.assertThat(domainCart.totalPrice).isEqualTo(cart.totalPrice)
            it.assertThat(domainCart.discountedPrice).isEqualTo(cart.discountedPrice)
            it.assertThat(domainCart.shippingPrice).isEqualTo(cart.shippingPrice)
            it.assertThat(domainCart.items.size).isEqualTo(cart.items.size)
        }
    }
}
