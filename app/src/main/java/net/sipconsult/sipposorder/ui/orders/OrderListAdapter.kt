package net.sipconsult.sipposorder.ui.orders

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import net.sipconsult.sipposorder.data.models.OrderItem
import net.sipconsult.sipposorder.databinding.ListItemOrderBinding
import java.util.*

class OrderListAdapter(private val onOrderClick: (OrderItem) -> Unit) :
    RecyclerView.Adapter<OrderViewHolder>(), Filterable {

    private var _orders: ArrayList<OrderItem> = arrayListOf()
    var orderFilter: ArrayList<OrderItem> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding =
            ListItemOrderBinding.inflate(layoutInflater)
//        val itemView = layoutInflater.inflate(R.layout.list_item_order, parent, false)
        return OrderViewHolder(binding, onOrderClick)
    }

    fun setOrders(orders: ArrayList<OrderItem>) {
        _orders = orders
        orderFilter = orders
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = orderFilter.size

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = orderFilter[position]
        holder.bind(order, position)
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                orderFilter = if (charSearch.isEmpty()) {
                    _orders
                } else {
                    val resultList: ArrayList<OrderItem> = arrayListOf()
                    for (order in _orders) {

                        if ((order.orderNumber.toLowerCase(Locale.ROOT)
                                .contains(charSearch.toLowerCase(Locale.ROOT)))
                        ) {
                            resultList.add(order)
                        }


                    }
                    resultList
                }
                val filterResults = FilterResults()
                filterResults.values = orderFilter
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                orderFilter = results?.values as ArrayList<OrderItem>

                notifyDataSetChanged()
            }

        }
    }
}