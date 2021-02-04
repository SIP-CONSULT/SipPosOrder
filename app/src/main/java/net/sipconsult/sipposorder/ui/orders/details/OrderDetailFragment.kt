package net.sipconsult.sipposorder.ui.orders.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import kotlinx.coroutines.launch
import net.sipconsult.sipposorder.SharedViewModel
import net.sipconsult.sipposorder.data.models.CartItem
import net.sipconsult.sipposorder.data.models.OrderItem
import net.sipconsult.sipposorder.data.models.OrderItemItem
import net.sipconsult.sipposorder.data.models.ProductItem
import net.sipconsult.sipposorder.databinding.OrderDetailFragmentBinding
import net.sipconsult.sipposorder.ui.base.ScopedFragment
import net.sipconsult.sipposorder.ui.ordercart.OrderCartAdapter
import net.sipconsult.sipposorder.ui.products.ProductListAdapter
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import java.text.DecimalFormat

class OrderDetailFragment : ScopedFragment(), KodeinAware {

    override val kodein by closestKodein()
    private var _binding: OrderDetailFragmentBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val viewModelFactory: OrderDetailViewModelFactory by instance()

    private lateinit var sharedViewModel: SharedViewModel

    private lateinit var viewModel: OrderDetailViewModel
    private lateinit var orderCartAdapter: OrderCartAdapter

    private val args: OrderDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = OrderDetailFragmentBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        sharedViewModel = activity?.run {
            ViewModelProvider(this)[SharedViewModel::class.java]
        } ?: throw Exception("Invalid Activity")

        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(OrderDetailViewModel::class.java)

        bindUI()

    }

    private fun bindUI() = launch {

        viewModel.orderId = args.orderId

        ldIn()

        val products = viewModel.getItems.await()
        if (view != null) {
            products.observe(viewLifecycleOwner, Observer { products ->
                if (products == null) return@Observer
                binding.groupLoadingOrderDetailProducts.visibility = View.GONE
                setupProductRecyclerView(products as java.util.ArrayList<ProductItem>)
                viewModel.productItems = products
            })

        }

        viewModel.orderResult.observe(
            viewLifecycleOwner, Observer { result ->
                result ?: return@Observer
//                groupOrderLoading.visibility = View.GONE

                result.error?.let {
//                    showVoucherFailed(it)
                }
                result.success?.let { order ->
                    updateUiWithOrder(order)
                    //        val products = viewModel.productItems
                }
            }
        )

        orderCartAdapter =
            OrderCartAdapter(
                ::onSubClick,
                ::onAddClick,
                ::onDeleteClick
            )
        binding.listOrderDetailCart.adapter = orderCartAdapter
        binding.listOrderDetailCart.addItemDecoration(
            DividerItemDecoration(
                binding.listOrderDetailCart.context,
                DividerItemDecoration.VERTICAL
            )
        )

        sharedViewModel.orderCartItems.observe(viewLifecycleOwner, Observer {
            if (it.isNullOrEmpty()) {
                binding.listOrderDetailCart.visibility = View.INVISIBLE
                binding.groupCart.visibility = View.VISIBLE
                binding.buttonOrderDetailProceed.isEnabled = false
//                buttonRefundClearProducts.isEnabled = false
            } else {
                binding.listOrderDetailCart.visibility = View.VISIBLE
                binding.groupCart.visibility = View.INVISIBLE
                binding.buttonOrderDetailProceed.isEnabled = true
//                buttonRefundClearProducts.isEnabled = true
            }
            orderCartAdapter.setCartItems(it.asReversed())
        })


        viewModel.totalCartPrice.observe(viewLifecycleOwner, Observer { totalPrice ->

            totalPrice?.let {
                val total = DecimalFormat("0.00").format(it)
                binding.textTotalOrderDetail.text = String.format("Total: Ghc %s", total)
            }

        })

        binding.buttonOrderDetialClearItems.setOnClickListener {
            viewModel.removeALLCartItem()
        }

        binding.buttonOrderDetailProceed.setOnClickListener {
//            sharedViewModel.orderType = 2
//            sharedViewModel.orderId = viewModel.orderId
//            sharedViewModel.setTotalPrice()

        }

//        setupRecyclerView(products as ArrayList<ProductItem>)
    }


    private fun ldIn() = launch {
        val result = viewModel.getOrder.await()
        viewModel.updateOrderResult(result)
    }

    private fun updateUiWithOrder(order: OrderItem) {
        binding.textOrderDetailDate.text = order.date
        binding.textOrderDetailOrderNumber.text = order.orderNumber
        binding.textOrderDetailLocation.text = order.location.name
//        textOrderTotalSales.text = order.totalSalesStr
        setupRecyclerView(order.orderItems as ArrayList<OrderItemItem>)
    }

    private fun setupRecyclerView(items: ArrayList<OrderItemItem>) {
        viewModel.removeALLCartItem()
        val cartItems: List<CartItem> = items.map { CartItem(ProductItem().fromOrderItem(it), quantity = it.quantity) };
        viewModel.addCartItems(cartItems)
    }


    private fun setupProductRecyclerView(products: java.util.ArrayList<ProductItem>) {
        val productRecyclerAdapter =
            ProductListAdapter(::onProductClick)
        binding.listOrderDetailProduct.adapter = productRecyclerAdapter
        productRecyclerAdapter.setProducts(products)
//        setupSearchView(productRecyclerAdapter)
    }

    private fun onProductClick(product: ProductItem) {
        viewModel.addCartItem(product)
    }


    private fun onDeleteClick(cartItem: CartItem) {
        viewModel.removeCartItem(cartItem)
    }

    private fun onSubClick(cartItem: CartItem) {
        viewModel.decreaseCartItemQuantity(cartItem)
    }

    private fun onAddClick(cartItem: CartItem) {
        viewModel.increaseCartItemQuantity(cartItem)
    }

}