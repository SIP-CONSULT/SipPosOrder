package net.sipconsult.sipposorder.ui.orders

import net.sipconsult.sipposorder.data.models.OrderItem

data class OrderResult(
    val success: OrderItem? = null,
    val error: Int? = null
) {
}