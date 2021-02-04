package net.sipconsult.sipposorder.ui.product

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import net.sipconsult.sipposorder.data.models.CartItem
import net.sipconsult.sipposorder.data.models.ProductItem
import net.sipconsult.sipposorder.data.repository.location.LocationRepository
import net.sipconsult.sipposorder.data.repository.product.ProductRepository
import net.sipconsult.sipposorder.data.repository.orderCart.OrderCartRepository
import net.sipconsult.sipposorder.internal.lazyDeferred

class ProductViewModel(
    private val productRepository: ProductRepository,
    private val locationRepository: LocationRepository
) : ViewModel() {

    var productItems: List<ProductItem> = arrayListOf()
    var localproducts: LiveData<List<ProductItem>> = productRepository.getProductsLocal()

    fun addCartItem(product: ProductItem) {
        val cartItem = CartItem(product)
        cartItem.let { OrderCartRepository.addCartItem(it) }
    }

    fun addScannedCartItem(barcode: String) {
        val pdt = productItems.find { p -> p.barcode == barcode }
        val cartItem = pdt?.let { CartItem(it) }
        cartItem.let { it?.let { it1 -> OrderCartRepository.addCartItem(it1) } }
    }

    val products by lazyDeferred {
        productRepository.getProducts()
    }

    val locations by lazyDeferred {
        locationRepository.getLocations()
    }
}