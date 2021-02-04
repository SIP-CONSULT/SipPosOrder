package net.sipconsult.sipposorder.data.models

import com.google.gson.annotations.SerializedName

class OrderItemItem(
    @SerializedName("id")
    val id: Int,
    @SerializedName("item")
    val product: ProductItem,
    @SerializedName("quantity")
    val quantity: Int,
    @SerializedName("orderId")
    val orderId: Int,
    @SerializedName("isActive")
    val isActive: Boolean
) {
}