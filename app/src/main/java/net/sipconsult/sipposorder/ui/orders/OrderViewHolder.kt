package net.sipconsult.sipposorder.ui.orders

import androidx.recyclerview.widget.RecyclerView
import net.sipconsult.sipposorder.data.models.OrderItem
import net.sipconsult.sipposorder.databinding.ListItemOrderBinding

class OrderViewHolder(
    val binding: ListItemOrderBinding,
    onOrderClick: (OrderItem) -> Unit
) :
    RecyclerView.ViewHolder(binding.root) {

    private var orderPosition: Int = 0
    private lateinit var _order: OrderItem

    fun bind(order: OrderItem, position: Int) {
        orderPosition = position
        _order = order
        binding.textOrderDate.text = order.getFormatDate()
        binding.textOrderOrderNumber.text = order.orderNumber
        binding.textOrderLocation.text = order.location.name
        binding.textSaleTransactionTotalSales.text = order.totalSales.toString()

    }

    init {
        itemView.setOnClickListener {
            onOrderClick(_order)
        }
    }
}