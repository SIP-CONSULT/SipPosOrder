package net.sipconsult.sipposorder.ui.orders

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import net.sipconsult.sipposorder.R
import net.sipconsult.sipposorder.data.network.response.Orders
import net.sipconsult.sipposorder.data.provider.LocationProvider
import net.sipconsult.sipposorder.data.repository.order.OrderRepository
import net.sipconsult.sipposorder.internal.Result
import net.sipconsult.sipposorder.internal.lazyDeferred

class OrderViewModel(
    private val orderRepository: OrderRepository,
    private val locationProvider: LocationProvider
) : ViewModel() {
//    private val locationCode = locationProvider.getLocation()

    private val _orderResult = MutableLiveData<OrdersResult>()
    val ordersResult: LiveData<OrdersResult> = _orderResult

    val getOrders by lazyDeferred {
        orderRepository.getOrdersSalesAgent(0)
    }

    fun updateTransactionResult(result: Result<Orders>) {
        if (result is Result.Success) {
            _orderResult.value =
                OrdersResult(
                    success = result.data
                )
        } else {
            _orderResult.value =
                OrdersResult(error = R.string.voucher_failed)
        }
    }
}