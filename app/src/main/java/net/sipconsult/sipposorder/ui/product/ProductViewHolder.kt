package net.sipconsult.sipposorder.ui.product

import androidx.recyclerview.widget.RecyclerView
import net.sipconsult.sipposorder.data.models.ProductItem
import net.sipconsult.sipposorder.databinding.ListItemProductBinding

class ProductViewHolder(
    val binding: ListItemProductBinding,
    onProductClick: (ProductItem) -> Unit
) :
    RecyclerView.ViewHolder(binding.root) {

    private var productPosition: Int = 0
    private lateinit var _product: ProductItem

    fun bind(product: ProductItem, position: Int) {
        productPosition = position
        _product = product
        binding.textProductName.text = product.name

        binding.textProductSalesPrice.text = product.displaySalesPrice()


//        GlideApp.with(itemView.context).load(R.drawable.juben_logo).into(itemView.imageProductName)
//        GlideApp.with(itemView.context).load(product.image)
//            .placeholder(R.drawable.juben_logo_landscape).into(itemView.imageProductName)
    }

    init {
        itemView.setOnClickListener {
            onProductClick(_product)
        }
    }
}