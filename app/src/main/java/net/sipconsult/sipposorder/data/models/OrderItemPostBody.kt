package net.sipconsult.sipposorder.data.models

import com.google.gson.annotations.SerializedName

class OrderItemPostBody(
    @SerializedName("itemId")
    val itemId: Int,
    @SerializedName("quantity")
    val quantity: Int
) {
}