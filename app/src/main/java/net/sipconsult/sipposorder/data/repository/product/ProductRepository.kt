package net.sipconsult.sipposorder.data.repository.product

import androidx.lifecycle.LiveData
import net.sipconsult.sipposorder.data.models.ProductItem

interface ProductRepository {
    suspend fun getProducts(): LiveData<List<ProductItem>>
    fun getProductsLocal(): LiveData<List<ProductItem>>

}