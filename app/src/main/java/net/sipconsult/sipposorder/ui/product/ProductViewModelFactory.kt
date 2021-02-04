package net.sipconsult.sipposorder.ui.product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import net.sipconsult.sipposorder.data.repository.location.LocationRepository
import net.sipconsult.sipposorder.data.repository.product.ProductRepository

class ProductViewModelFactory(
    private val productRepository: ProductRepository,
    private val locationRepository: LocationRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ProductViewModel(productRepository, locationRepository) as T
    }
}