package net.sipconsult.sipposorder.data.repository.order

import androidx.lifecycle.LiveData
import net.sipconsult.sipposorder.data.models.OrderItem
import net.sipconsult.sipposorder.data.models.OrderPostBody
import net.sipconsult.sipposorder.data.network.response.OrderResponse
import net.sipconsult.sipposorder.data.network.response.Orders
import net.sipconsult.sipposorder.internal.Result

interface OrderRepository {
    suspend fun postOrder(body: OrderPostBody): Result<OrderResponse>
    suspend fun getOrdersSalesAgent(salesAgentId: Int): Result<Orders>

    suspend fun fetchOrder(orderId: Int): Result<OrderItem>


}