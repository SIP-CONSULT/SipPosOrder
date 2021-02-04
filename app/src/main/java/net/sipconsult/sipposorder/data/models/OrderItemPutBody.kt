package net.sipconsult.sipposorder.data.models

import com.google.gson.annotations.SerializedName

data class OrderItemPutBody(
    @SerializedName("itemId")
    val itemId: Int,
    @SerializedName("quantity")
    val quantity: Int
)
