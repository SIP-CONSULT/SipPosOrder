package net.sipconsult.sipposorder.ui.orders.details

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import kotlinx.coroutines.launch
import net.sipconsult.sipposorder.MainActivity
import net.sipconsult.sipposorder.SharedViewModel
import net.sipconsult.sipposorder.data.models.CartItem
import net.sipconsult.sipposorder.data.models.OrderItem
import net.sipconsult.sipposorder.data.models.OrderItemItem
import net.sipconsult.sipposorder.data.models.ProductItem
import net.sipconsult.sipposorder.databinding.OrderDetailFragmentBinding
import net.sipconsult.sipposorder.ui.base.ScopedFragment
import net.sipconsult.sipposorder.ui.ordercart.OrderCartAdapter
import net.sipconsult.sipposorder.ui.product.ProductListAdapter
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
    ): View {
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

        sharedViewModel.orderResult.observe(
            viewLifecycleOwner,
            Observer { result ->
                result ?: return@Observer
                binding.groupLoadingOrderDetailProducts.visibility = View.GONE

                result.getContentIfNotHandled()?.let { transactionResult ->

                    transactionResult.error?.let {
                        showTransactionFailed(it)
                    }
                    transactionResult.success?.let {
                        sharedViewModel.resetTransaction()
                        result.hasBeenHandled()
//                        navigateToReceipt()
                        sharedViewModel.resetAll()
                        activity?.finish()
                        val intent = Intent(context, MainActivity::class.java)
                        startActivity(intent)

                    }
                }


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

            binding.groupLoadingOrderDetailProducts.visibility = View.VISIBLE
            postOrder()

        }

//        setupRecyclerView(products as ArrayList<ProductItem>)
    }

    private fun postOrder() = launch {
        val result = sharedViewModel.postOrderS.await()
        sharedViewModel.updateOrderResult(result)
    }

    private fun ldIn() = launch {
        val result = viewModel.getOrder.await()
        viewModel.updateOrderResult(result)
    }

    private fun updateUiWithOrder(order: OrderItem) {
        sharedViewModel.orderId = order.id
        binding.textOrderDetailDate.text = order.getFormatDate()
        binding.textOrderDetailOrderNumber.text = order.orderNumber
        binding.textOrderDetailLocation.text = order.location.name
//        textOrderTotalSales.text = order.totalSalesStr
        setupRecyclerView(order.orderItems as ArrayList<OrderItemItem>)
    }

    private fun setupRecyclerView(items: ArrayList<OrderItemItem>) {
        viewModel.removeALLCartItem()
        val cartItems: List<CartItem> =
            items.map { CartItem(ProductItem().fromOrderItem(it), quantity = it.quantity) };
        viewModel.addCartItems(cartItems)
    }

    private fun setupProductRecyclerView(products: ArrayList<ProductItem>) {
        val productRecyclerAdapter =
            ProductListAdapter(::onProductClick)
        binding.listOrderDetailProduct.adapter = productRecyclerAdapter
        productRecyclerAdapter.setProducts(products)
//        setupSearchView(productRecyclerAdapter)
    }

    private fun showTransactionFailed(@StringRes errorString: Int) {
        val appContext = context?.applicationContext ?: return
        Toast.makeText(appContext, errorString, Toast.LENGTH_LONG).show()
//        sharedViewModel.resetAll()
//                findNavController().navigate(R.id.nav_home)
//                (activity as MainActivity).recreate()
//                activity?.viewModelStore?.clear()
        activity?.finish()
        val intent = Intent(context, MainActivity::class.java)
        startActivity(intent)
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