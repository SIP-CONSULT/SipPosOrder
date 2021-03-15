package net.sipconsult.sipposorder.data.models

import com.google.gson.annotations.SerializedName
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

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

    fun getFormatDate(): String {
        val df: DateFormat = SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss", Locale.getDefault())
        var result: Date? = null
        return try {
            result = df.parse(this.date)
            println("date:$result") //prints date in current locale

            val sdf = SimpleDateFormat("dd, MMM yyyy hh:mm aaa", Locale.getDefault())
            sdf.timeZone = TimeZone.getTimeZone("GMT")
            println(sdf.format(result)) //prints date in the format sdf

            sdf.format(result)

        } catch (e: Exception) {
            ""
        }
    }


}