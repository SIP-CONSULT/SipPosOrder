package net.sipconsult.sipposorder.data.models


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName


@Entity(
    tableName = "products"
)
data class ProductItem(
    @SerializedName("id")
    @PrimaryKey(autoGenerate = false)
    val id: Int,

    @SerializedName("code")
    @ColumnInfo(name = "product_code")
    val code: String?,

    @SerializedName("name")
    @ColumnInfo(name = "product_name")
    val name: String,

    @SerializedName("image")
    @ColumnInfo(name = "image")
    val image: String,

    @SerializedName("costPrice")
    @ColumnInfo(name = "cost_price")
    val costPrice: Double?,

    @SerializedName("salePrice")
    @ColumnInfo(name = "sale_price")
    val salePrice: Double,

    @SerializedName("quantity")
    val quantity: Int,

    @SerializedName("barcode")
    val barcode: String?


) {

    constructor() : this(0, "", "", "", 0.0, 0.0, 1, "") {
    }

    fun displaySalesPrice(): String {
        return String.format("GHC %.2f ", salePrice)

    }

    fun displaySalesPriceDiscount(): String =
        String.format("GHC %.2f ", salePrice)

    fun fromOrderItem(orderItem: OrderItemItem): ProductItem {
        val pdt = ProductItem(
            id = orderItem.id,
            barcode = orderItem.product.barcode,
            name = orderItem.product.name,
            code = orderItem.product.code,
            salePrice = orderItem.product.salePrice,
            costPrice = orderItem.product.salePrice,
            quantity = orderItem.quantity,
            image = orderItem.product.image
        )
        pdt.isActive = orderItem.isActive;
        return pdt
    }

    @Ignore
    var isActive: Boolean = false
}
/*

data class ProductItem(
    @SerializedName("id")
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    @SerializedName("categoryId")
    @ColumnInfo(name = "category_id")
    val categoryId: Int,
    @SerializedName("description")
    val description: String?,
    @SerializedName("imageUrl")
    @ColumnInfo(name = "image_url")
    val imageUrl: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("salesPrice")
    @ColumnInfo(name = "sale_price")
    val salePrice: Double,
    @SerializedName("discount")
    val discount: Int,
    @SerializedName("costPrice")
    @ColumnInfo(name = "cost_price")
    val costPrice: Double,
    @SerializedName("barcode")
    val barcode: Long?,
    @SerializedName("quantity")
    val quantity: Int
) {

    fun displaySalesPrice(): String? {
        return String.format("GHC %.2f ", salePrice)

    }

    fun displaySalesPriceDiscount(): String? {
        return if (discount > 1) {
            val salePriceDiscount = discount.toDouble() / 100
            val discountPrice: Double = salePriceDiscount * salePrice
            val newSalesPrice: Double = salePrice - discountPrice
            String.format("GHC %.2f", newSalesPrice)
        } else {
            String.format("GHC %.2f ", salePrice)
        }
    }
}*/
