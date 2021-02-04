package net.sipconsult.sipposorder.data.models

import com.google.gson.annotations.SerializedName

data class OrderPutBody(
    @SerializedName("id")
    val id: Int,
    @SerializedName("orderItems")
    val orderItems: List<OrderItemPutBody>
)