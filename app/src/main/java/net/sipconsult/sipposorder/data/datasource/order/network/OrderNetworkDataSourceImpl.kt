package net.sipconsult.sipposorder.data.datasource.order.network

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import net.sipconsult.sipposorder.data.models.OrderItem
import net.sipconsult.sipposorder.data.models.OrderPostBody
import net.sipconsult.sipposorder.data.network.SipShopApiService
import net.sipconsult.sipposorder.data.network.response.OrderResponse
import net.sipconsult.sipposorder.data.network.response.Orders
import net.sipconsult.sipposorder.internal.NoConnectivityException
import net.sipconsult.sipposorder.internal.Result
import java.io.IOException

class OrderNetworkDataSourceImpl(
    private val sipShopApiService: SipShopApiService
) : OrderNetworkDataSource {

    private val _downloadOrders = MutableLiveData<Orders>()
    override suspend fun postOrder(body: OrderPostBody): Result<OrderResponse> {
        try {
            val postOrder = sipShopApiService.postOrderAsync(body)

            return if (postOrder.successful) {
                Result.Success(
                    postOrder
                )

            } else {
                Result.Error(
                    IOException("Error logging in")
                )
            }
        } catch (e: NoConnectivityException) {
            Log.d(TAG, "postOrder: No internet Connection ", e)
            return Result.Error(
                IOException("Error logging in", e)
            )
        }
    }

    override suspend fun fetchOrderSalesAgent(salesAgentId: Int): Result<Orders> {
        return try {

            val call = sipShopApiService.getOrdersAsync()

            Result.Success(
                call
            )

        } catch (e: Throwable) {
            return Result.Error(
                IOException("Error logging in", e)
            )
        }
    }

    override suspend fun fetchOrder(orderId: Int): Result<OrderItem> {
        return try {

            val call = sipShopApiService.getOrderAsync(orderId)

            Result.Success(
                call
            )

        } catch (e: Throwable) {
            return Result.Error(
                IOException("Error logging in", e)
            )
        }
    }

    override val downloadOrders: LiveData<Orders>
        get() = _downloadOrders

    override suspend fun fetchOrders() {
        try {
            val fetchedOrders = sipShopApiService.getOrdersAsync()
            _downloadOrders.postValue(fetchedOrders)
        } catch (e: NoConnectivityException) {
            Log.d(TAG, "fetchOrders: No internet Connection ", e)
        }
    }

    companion object {
        private const val TAG: String = "OrderNetworkDataSrc"
    }
}