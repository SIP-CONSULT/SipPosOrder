package net.sipconsult.sipposorder.ui.ordercart

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import net.sipconsult.sipposorder.data.models.CartItem
import net.sipconsult.sipposorder.databinding.ListItemCartBinding

class OrderCartViewHolder(
    private val binding: ListItemCartBinding,
    onSubClick: (CartItem) -> Unit,
    onAddClick: (CartItem) -> Unit,
    onDeleteClick: (CartItem) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    private var cartItemPosition: Int = 0
    private lateinit var _cartItem: CartItem

    fun bind(cartItem: CartItem, position: Int) {
        cartItemPosition = position
        _cartItem = cartItem
        binding.textProductName.text = cartItem.product.name
//        binding.textProductDescription.visibility = View.GONE

        binding.textQuantity.text = cartItem.getProductQuantityString()

        binding.textProductSalesPrice.text = cartItem.product.displaySalesPrice()

//        GlideApp.with(itemView.context).load(cartItem.product.image)
//            .placeholder(R.drawable.company_logo).into(itemView.imageProduct)
    }

    init {
        binding.imageButtonDelete.setOnClickListener {
            onDeleteClick(_cartItem)
        }
        binding.imageButtonSub.setOnClickListener {
            onSubClick(_cartItem)
        }
        binding.imageButtonAdd.setOnClickListener {
            onAddClick(_cartItem)
        }
    }


}