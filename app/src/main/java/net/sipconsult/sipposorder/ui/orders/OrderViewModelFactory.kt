package net.sipconsult.sipposorder.ui.orders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import net.sipconsult.sipposorder.data.provider.LocationProvider
import net.sipconsult.sipposorder.data.repository.location.LocationRepository
import net.sipconsult.sipposorder.data.repository.order.OrderRepository
import net.sipconsult.sipposorder.data.repository.product.ProductRepository

class OrderViewModelFactory(
    private val orderRepository: OrderRepository,
    private val locationProvider: LocationProvider
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return OrderViewModel(orderRepository, locationProvider) as T
    }
}