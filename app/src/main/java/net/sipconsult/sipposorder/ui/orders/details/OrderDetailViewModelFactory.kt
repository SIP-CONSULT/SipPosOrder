package net.sipconsult.sipposorder.ui.orders.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import net.sipconsult.sipposorder.data.repository.order.OrderRepository
import net.sipconsult.sipposorder.data.repository.product.ProductRepository

class OrderDetailViewModelFactory(
    private val orderRepository: OrderRepository,
    private val productRepository: ProductRepository
) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return OrderDetailViewModel(
            orderRepository,
            productRepository
        ) as T
    }
}