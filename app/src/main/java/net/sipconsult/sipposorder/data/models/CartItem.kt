package net.sipconsult.sipposorder.data.models

data class CartItem(var product: ProductItem, var quantity: Int = 1) {

    fun getProductPriceString(): String = String.format("Ghc %.2f", product.salePrice)

    fun getProductQuantityString(): String = String.format("%d", quantity)

}