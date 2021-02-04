package net.sipconsult.sipposorder.data.models

import com.google.gson.annotations.SerializedName

class OrderItem(
    @SerializedName("dateTime")
    val date: String,
    @SerializedName("location")
    val location: LocationsItem,
    @SerializedName("id")
    val id: Int,
    @SerializedName("totalSales")
    val totalSales: Double,
    @SerializedName("orderNumber")
    val orderNumber: String,
    @SerializedName("salesAgent")
    val salesAgent: SalesAgentsItem,
    @SerializedName("orderItems")
    val orderItems: List<OrderItemItem>
) {
}