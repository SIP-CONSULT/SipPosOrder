package net.sipconsult.sipposorder.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import net.sipconsult.sipposorder.data.models.CartItem
import net.sipconsult.sipposorder.data.repository.orderCart.OrderCartRepository

class HomeViewModel : ViewModel() {

    val cartItems: LiveData<MutableList<CartItem>> = OrderCartRepository.cartItems

    val totalPrice: LiveData<Double> = OrderCartRepository.totalPrice


    fun removeCartItem(cartItem: CartItem) {
        OrderCartRepository.removeCartItem(cartItem)
    }

    fun increaseCartItemQuantity(cartItem: CartItem) {
        OrderCartRepository.increaseCartItemQuantity(cartItem)
    }

    fun decreaseCartItemQuantity(cartItem: CartItem) {
        OrderCartRepository.decreaseCartItemQuantity(cartItem)
    }

    fun removeALLCartItem() {
        OrderCartRepository.removeALLCartItem()
    }

}