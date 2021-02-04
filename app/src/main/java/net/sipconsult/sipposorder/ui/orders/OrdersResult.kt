package net.sipconsult.sipposorder.ui.orders

import net.sipconsult.sipposorder.data.network.response.Orders


data class OrdersResult(
    val success: Orders? = null,
    val error: Int? = null
)