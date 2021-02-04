package net.sipconsult.sipposorder.data.models

import com.google.gson.annotations.SerializedName

class OrderPostBody(
    @SerializedName("salesAgentId")
    val salesAgentId: Int,
    @SerializedName("locationId")
    val locationId: Int,
    @SerializedName("orderNumber")
    val orderNumber: String,
    @SerializedName("OrderItems")
    val orderItems: List<OrderItemPostBody>
) {
}