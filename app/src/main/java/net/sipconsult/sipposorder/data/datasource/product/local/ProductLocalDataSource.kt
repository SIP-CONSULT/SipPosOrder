package net.sipconsult.sipposorder.data.datasource.product.local

import androidx.lifecycle.LiveData
import net.sipconsult.sipposorder.data.models.ProductItem

interface ProductLocalDataSource {
    val products: LiveData<List<ProductItem>>

    fun updateProducts(products: List<ProductItem>)
}