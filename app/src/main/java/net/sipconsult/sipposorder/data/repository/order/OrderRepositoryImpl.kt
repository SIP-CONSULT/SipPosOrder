package net.sipconsult.sipposorder.data.repository.order

import androidx.lifecycle.LiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import net.sipconsult.sipposorder.data.datasource.order.local.OrderLocalDataSource
import net.sipconsult.sipposorder.data.datasource.order.network.OrderNetworkDataSource
import net.sipconsult.sipposorder.data.models.OrderItem
import net.sipconsult.sipposorder.data.models.OrderPostBody
import net.sipconsult.sipposorder.data.network.response.OrderResponse
import net.sipconsult.sipposorder.data.network.response.Orders
import net.sipconsult.sipposorder.internal.Result

class OrderRepositoryImpl(
    private val networkDataSource: OrderNetworkDataSource,
    private val localDataSource: OrderLocalDataSource
) : OrderRepository {

    init {
        networkDataSource.downloadOrders.observeForever { currentOrders ->
            persistFetchedOrders(currentOrders)
        }
    }

    override suspend fun postOrder(body: OrderPostBody): Result<OrderResponse> {
        return withContext(Dispatchers.IO) {
            return@withContext networkDataSource.postOrder(body)
        }
    }

    override suspend fun getOrdersSalesAgent(salesAgentId: Int): Result<Orders> {
        return withContext(Dispatchers.IO) {
//            initOrdersData()
            return@withContext networkDataSource.fetchOrderSalesAgent(salesAgentId)
        }
    }

    override suspend fun fetchOrder(orderId: Int): Result<OrderItem> {
        return withContext(Dispatchers.IO) {
            return@withContext networkDataSource.fetchOrder(orderId)
        }
    }


    private fun persistFetchedOrders(fetchedOrders: List<OrderItem>) {
        GlobalScope.launch(Dispatchers.IO) {
//            localDataSource.updateOrders(fetchedOrders)
        }
    }

    private suspend fun initOrdersData() {
        fetchOrders()

    }

    private suspend fun fetchOrders() {
        networkDataSource.fetchOrders()
    }
}