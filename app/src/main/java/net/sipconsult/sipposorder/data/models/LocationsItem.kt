package net.sipconsult.sipposorder.data.models


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(
    tableName = "locations"
)
data class LocationsItem(
    @SerializedName("id")
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    @SerializedName("code")
    val code: String? = "",
    @SerializedName("name")
    val name: String = "",
    @SerializedName("mobilePhoneNumber1")
    @ColumnInfo(name = "mobile_phone_number_one")
    val mobilePhoneNumber1: String = "",
    @SerializedName("mobilePhoneNumber2")
    @ColumnInfo(name = "mobile_phone_number_two")
    val mobilePhoneNumber2: String? = "",
    @SerializedName("address")
    val address: String? = "",
    @SerializedName("telephoneNumber")
    @ColumnInfo(name = "telephone_number")
    val telephoneNumber: String? = "",
    @SerializedName("company")
    val company: String = "",
    @SerializedName("message")
    val message: String = ""
)