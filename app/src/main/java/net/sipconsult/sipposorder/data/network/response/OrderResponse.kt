package net.sipconsult.sipposorder.data.network.response

import com.google.gson.annotations.SerializedName

class OrderResponse(
    @SerializedName("error")
    val error: String,
    @SerializedName("successful")
    val successful: Boolean
)