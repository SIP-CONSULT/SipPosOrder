package net.sipconsult.sipposorder.ui.orders

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import kotlinx.coroutines.launch
import net.sipconsult.sipposorder.data.models.OrderItem
import net.sipconsult.sipposorder.databinding.OrderFragmentBinding
import net.sipconsult.sipposorder.ui.base.ScopedFragment
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class OrderFragment : ScopedFragment(), KodeinAware {

    override val kodein by closestKodein()

    private var _binding: OrderFragmentBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val viewModelFactory: OrderViewModelFactory by instance()

    private lateinit var viewModel: OrderViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = OrderFragmentBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(OrderViewModel::class.java)

        bindUI()
    }

    private fun ldIn() = launch {
        val result = viewModel.getOrders.await()
        viewModel.updateTransactionResult(result)
    }

    private fun bindUI() = launch {

        ldIn()
        if (view != null) {
            viewModel.ordersResult.observe(
                viewLifecycleOwner,
                Observer { result ->
                    result ?: return@Observer
                    binding.groupOrdersLoading.visibility = View.GONE

                    result.error?.let {
//                    showVoucherFailed(it)
                    }
                    result.success?.let {
                        setupOrdersRecyclerView(it)
                    }
                })
        }

    }

    private fun Context.hideKeyboard(view: View) {
        val inputMethodManager =
            getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun Fragment.hideKeyboard() {
        view?.let {
            activity?.hideKeyboard(it)
        }
    }

    private fun setupSearchView(orderListAdapter: OrderListAdapter) {
        binding.searchOrders.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                hideKeyboard()
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                orderListAdapter.filter.filter(newText)
                return false
            }

        })
    }


    private fun setupOrdersRecyclerView(orders: ArrayList<OrderItem>) {
        val orderListAdapter =
            OrderListAdapter(::onTransactionClick)
        binding.listOrders.adapter = orderListAdapter
        binding.listOrders.addItemDecoration(
            DividerItemDecoration(
                binding.listOrders.context,
                DividerItemDecoration.VERTICAL
            )
        )
        orderListAdapter.setOrders(orders)
        setupSearchView(orderListAdapter)
    }

    private fun onTransactionClick(item: OrderItem) {
        val action =
            OrderFragmentDirections.actionNavOrdersToOrderDetailFragment(item.id)
        this.findNavController().navigate(action)
    }
}