package net.sipconsult.sipposorder.ui.orders.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import net.sipconsult.sipposorder.R
import net.sipconsult.sipposorder.data.models.CartItem
import net.sipconsult.sipposorder.data.models.OrderItem
import net.sipconsult.sipposorder.data.models.ProductItem
import net.sipconsult.sipposorder.data.repository.order.OrderRepository
import net.sipconsult.sipposorder.data.repository.product.ProductRepository
import net.sipconsult.sipposorder.data.repository.orderCart.OrderCartRepository
import net.sipconsult.sipposorder.internal.Result
import net.sipconsult.sipposorder.internal.lazyDeferred
import net.sipconsult.sipposorder.ui.orders.OrderResult

class OrderDetailViewModel(
    private val orderRepository: OrderRepository,
    private val productRepository: ProductRepository
) : ViewModel() {
    var orderId: Int = 0
    var productItems: List<ProductItem> = arrayListOf()

    private val _orderResult = MutableLiveData<OrderResult>()
    val orderResult: LiveData<OrderResult> = _orderResult

    val cartItems: LiveData<MutableList<CartItem>> = OrderCartRepository.orderCartItems

    val totalCartPrice: LiveData<Double> = OrderCartRepository.orderTotalPrice


    val getOrder by lazyDeferred {
        orderRepository.fetchOrder(orderId)
    }

    val getItems by lazyDeferred {
        productRepository.getProducts()
    }

    fun updateOrderResult(result: Result<OrderItem>) {
        if (result is Result.Success) {
            _orderResult.value =
                OrderResult(
                    success = result.data
                )
        } else {
            _orderResult.value =
                OrderResult(error = R.string.voucher_failed)
        }
    }

    fun addCartItem(product: ProductItem) {
        val cartItem = CartItem(product)
        cartItem.let { OrderCartRepository.addOrderCartItem(it) }
    }

    fun addCartItems(cartItems: List<CartItem>) {
        OrderCartRepository.addOrderCartItems(cartItems)
    }

    fun removeCartItem(cartItem: CartItem) {
        OrderCartRepository.removeOrderCartItem(cartItem)
    }

    fun increaseCartItemQuantity(cartItem: CartItem) {
        OrderCartRepository.increaseOrderCartItemQuantity(cartItem)
    }

    fun decreaseCartItemQuantity(cartItem: CartItem) {
        OrderCartRepository.decreaseOrderCartItemQuantity(cartItem)
    }

    fun removeALLCartItem() {
        OrderCartRepository.removeALLOrderCartItem()
    }
}