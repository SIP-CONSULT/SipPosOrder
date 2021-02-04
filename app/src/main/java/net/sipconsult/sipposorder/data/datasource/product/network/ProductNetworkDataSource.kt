package net.sipconsult.sipposorder.data.datasource.product.network

import androidx.lifecycle.LiveData
import net.sipconsult.sipposorder.data.network.response.Products

interface ProductNetworkDataSource {
    val downloadProducts: LiveData<Products>

    suspend fun fetchProducts()
}