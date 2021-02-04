package net.sipconsult.sipposorder.data.repository.orderCart

import android.os.AsyncTask
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.paperdb.Paper
import net.sipconsult.sipposorder.data.models.CartItem
import java.text.DecimalFormat

object OrderCartRepository {

    private const val CART: String = "cart"
    private const val ORDER_CART: String = "order_cart"

    val decimalFormater = DecimalFormat("0.00")


    private val _emptyCartItems = MutableLiveData<MutableList<CartItem>>()
    val emptyCartItems: LiveData<MutableList<CartItem>>
        get() = _emptyCartItems

    private val _cartItems = MutableLiveData<MutableList<CartItem>>()
    val cartItems: LiveData<MutableList<CartItem>>
        get() = _cartItems

    private val _orderCartItems = MutableLiveData<MutableList<CartItem>>()
    val orderCartItems: LiveData<MutableList<CartItem>>
        get() = _orderCartItems

    private val _totalPrice = MutableLiveData<Double>()
    val totalPrice: LiveData<Double>
        get() = _totalPrice

    private val _orderTotalPrice = MutableLiveData<Double>()
    val orderTotalPrice: LiveData<Double>
        get() = _orderTotalPrice

    val totalCartPrice: Double
        get() = getCartPrice()

    val refundTotalCartPrice: Double
        get() = getOrderCartPrice()

    private val _totalQuantity = MutableLiveData<Int>()

    val totalQuantity: LiveData<Int>
        get() = _totalQuantity

    init {
        setCartItems()
        setOrderCartItems()

        setTotalPrice()
        setOrderTotalPrice()

        _totalQuantity.postValue(getCartQuantity())
    }


    private fun setCartItems() {
        _cartItems.value = getCart()
    }

    fun postCartItems() {
        _cartItems.postValue(getCart())
    }

    private fun setOrderCartItems() {
//        _cartItems.postValue(getCart())
        _orderCartItems.value = getOrderCart()
    }

    private fun setTotalPrice() {
//        _totalPrice.postValue(getCartPrice())
        _totalPrice.value = getCartPrice()
    }

    fun postTotalPrice() {
        _totalPrice.postValue(getCartPrice())
    }

    private fun setOrderTotalPrice() {
//        _totalPrice.postValue(getCartPrice())
        _orderTotalPrice.value = getOrderCartPrice()
    }

    fun addCartItem(cartItem: CartItem) {
        val cart =
            getCart()

        val targetItem = cart.singleOrNull { it.product.id == cartItem.product.id }
        if (targetItem == null) {
            cart.add(cartItem)
        } else {
            targetItem.quantity++
        }

        saveCart(
            cart
        )
        setCartItems()
        setTotalPrice()
//        AddCartItemAsyncTask().execute(cartItem)
    }

    fun addOrderCartItem(cartItem: CartItem) {
        val cart =
            getOrderCart()

        val targetItem = cart.singleOrNull { it.product.id == cartItem.product.id }
        if (targetItem == null) {
            cart.add(cartItem)
        } else {
            targetItem.quantity++
        }

        saveOrderCart(
            cart
        )

        setOrderCartItems()
        setOrderTotalPrice()
//        AddCartItemAsyncTask().execute(cartItem)
    }

    fun addOrderCartItems(cartItems: List<CartItem>) {
        val cart =
            getOrderCart()

        cart.addAll(cartItems)

        saveOrderCart(
            cart
        )

        setOrderCartItems()
        setOrderTotalPrice()
//        AddCartItemAsyncTask().execute(cartItem)
    }

    fun removeCartItem(cartItem: CartItem) {
        val cart =
            getCart()
        val targetItem = cart.singleOrNull { it.product.id == cartItem.product.id }
//        val cartItem = cart[cartItem]

        cart.remove(targetItem)

        saveCart(
            cart
        )

        setCartItems()
        setTotalPrice()
//        RemoveCartItemAsyncTask().execute(item)
    }

    fun removeOrderCartItem(cartItem: CartItem) {
        val cart =
            getOrderCart()
        val targetItem = cart.singleOrNull { it.product.id == cartItem.product.id }
        if (!cartItem.product.isActive) {
            cart.remove(targetItem)
        }

        saveOrderCart(
            cart
        )

        setOrderCartItems()
        setOrderTotalPrice()
//        RemoveCartItemAsyncTask().execute(item)
    }


    fun increaseCartItemQuantity(cartItem: CartItem) {
        val cart =
            getCart()

        val targetItem = cart.singleOrNull { it.product.id == cartItem.product.id }
        if (targetItem != null) {
            targetItem.quantity++
        }
        saveCart(
            cart
        )

        setCartItems()
        setTotalPrice()

//        IncreaseCartItemQuantityAsyncTask().execute(item)
    }

    fun increaseOrderCartItemQuantity(cartItem: CartItem) {
        val cart =
            getOrderCart()

        val targetItem = cart.singleOrNull { it.product.id == cartItem.product.id }
        if (targetItem != null) {
            targetItem.quantity++
        }

        saveOrderCart(
            cart
        )

        setOrderCartItems()
        setOrderTotalPrice()

//        IncreaseCartItemQuantityAsyncTask().execute(item)
    }

    fun decreaseCartItemQuantity(cartItem: CartItem) {
        val cart =
            getCart()
        val targetItem = cart.singleOrNull { it.product.id == cartItem.product.id }
        if (targetItem != null) {
            if (targetItem.quantity > 1) {
                targetItem.quantity--
            }
        }

        saveCart(
            cart
        )

        setCartItems()
        setTotalPrice()

//        DecreaseCartItemQuantityAsyncTask().execute(item)
    }

    fun decreaseOrderCartItemQuantity(cartItem: CartItem) {
        val cart =
            getOrderCart()
        val targetItem = cart.singleOrNull { it.product.id == cartItem.product.id }
        if (targetItem != null) {

            if (!cartItem.product.isActive) {
                if (targetItem.quantity > 1) {
                    targetItem.quantity--
                }
            } else {
                if (targetItem.quantity > cartItem.product.quantity) {
                    targetItem.quantity--
                }
            }
        }

        saveOrderCart(
            cart
        )

        setOrderCartItems()
        setOrderTotalPrice()

//        DecreaseCartItemQuantityAsyncTask().execute(item)
    }

    fun removeALLCartItem() {
        deleteCart()
        setCartItems()
        setTotalPrice()
    }

    fun removeALLOrderCartItem() {
        deleteOrderCart()
        setOrderCartItems()
        setOrderTotalPrice()
    }

    private fun saveCart(cart: MutableList<CartItem>) {
        Paper.book().write(CART, cart)
    }

    private fun saveOrderCart(cart: MutableList<CartItem>) {
        Paper.book().write(ORDER_CART, cart)
    }

    fun getCart(): MutableList<CartItem> {
        return Paper.book().read(CART, mutableListOf())
    }

    private fun getOrderCart(): MutableList<CartItem> {
        return Paper.book().read(ORDER_CART, mutableListOf())
    }

    fun deleteCart() = Paper.book().delete(CART)

    fun deleteOrderCart() = Paper.book().delete(ORDER_CART)

    fun getShoppingCartSize(): Int {
        var cartSize = 0
        getCart()
            .forEach {
                cartSize += it.quantity
            }

        return cartSize
    }

    private fun getCartPrice(): Double {
        var price = 0.0
        _cartItems.value?.forEach {
            price += (it.product.salePrice * it.quantity)
        }
        return price
    }

    private fun getOrderCartPrice(): Double {
        var price = 0.0
        _orderCartItems.value?.forEach {
            price += (it.product.salePrice * it.quantity)
        }
        return price
    }

    private fun getCartQuantity(): Int {
        var quantity = 0
        _cartItems.value?.forEach {
            quantity += it.quantity
        }
        return quantity
    }


    //    companion object {
    class AddCartItemAsyncTask :
        AsyncTask<CartItem, Void, Void>() {
        override fun doInBackground(vararg params: CartItem): Void? {
            val cart =
                getCart()

            val targetItem = cart.singleOrNull { it.product.id == params[0].product.id }
            if (targetItem == null) {
                cart.add(params[0])
            }

            saveCart(
                cart
            )
            return null
        }

    }

    class RemoveCartItemAsyncTask : AsyncTask<Int, Void, Void>() {
        override fun doInBackground(vararg params: Int?): Void? {
            val cart =
                getCart()
            val cartItem = cart[params[0]!!]
            cart.remove(cartItem)
            saveCart(
                cart
            )
            return null
        }
    }

    class IncreaseCartItemQuantityAsyncTask :
        AsyncTask<Int, Void, Void>() {
        override fun doInBackground(vararg params: Int?): Void? {
            val cart =
                getCart()
            val cartItem = cart[params[0]!!]

            val targetItem = cart.singleOrNull { it.product.id == cartItem.product.id }
            if (targetItem != null) {
                targetItem.quantity++
                Log.d(
                    "ShoppingCartRepository",
                    "increaseCartItemQuantity: ${targetItem.quantity}"
                )
            }
            saveCart(
                cart
            )
            return null
        }
    }

    class DecreaseCartItemQuantityAsyncTask :
        AsyncTask<Int, Void, Void>() {
        override fun doInBackground(vararg params: Int?): Void? {
            val cart =
                getCart()
            val cartItem = cart[params[0]!!]
            val targetItem = cart.singleOrNull { it.product.id == cartItem.product.id }
            if (targetItem != null) {
                if (targetItem.quantity > 1) {
                    targetItem.quantity--
                    Log.d(
                        "ShoppingCartRepository",
                        "decreaseCartItemQuantity: ${targetItem.quantity}"
                    )
                }
            }
//
            saveCart(
                cart
            )
            return null
        }
    }

    class RemoveALLCartItemAsyncTask :
        AsyncTask<Int, Void, Void>() {
        override fun doInBackground(vararg params: Int?): Void? {
            deleteCart()
            return null
        }
    }


//    }


}