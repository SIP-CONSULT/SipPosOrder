package net.sipconsult.sipposorder.ui.ordercart

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import net.sipconsult.sipposorder.data.models.CartItem
import net.sipconsult.sipposorder.databinding.ListItemCartBinding

class OrderCartAdapter(
    private val onSubClick: (CartItem) -> Unit,
    private val onAddClick: (CartItem) -> Unit,
    private val onDeleteClick: (CartItem) -> Unit
) : RecyclerView.Adapter<OrderCartViewHolder>() {

    private var _cartItems: List<CartItem> = listOf()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderCartViewHolder {

        val layoutInflater = LayoutInflater.from(parent.context)
        val binding =
            ListItemCartBinding.inflate(layoutInflater)
//        val itemView = layoutInflater.inflate(R.layout.list_item_cart, parent, false)

        return OrderCartViewHolder(
            binding,
            onSubClick,
            onAddClick,
            onDeleteClick
        )
    }

    fun setCartItems(cartItems: List<CartItem>) {
        _cartItems = cartItems
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = _cartItems.size

    override fun onBindViewHolder(holder: OrderCartViewHolder, position: Int) {
        with(holder) {
            with(_cartItems[position]) {
                bind(this, position)
            }
        }

    }
}