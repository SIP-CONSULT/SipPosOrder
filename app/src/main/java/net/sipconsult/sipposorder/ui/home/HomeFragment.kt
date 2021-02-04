package net.sipconsult.sipposorder.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.DividerItemDecoration
import kotlinx.coroutines.launch
import net.sipconsult.sipposorder.MainActivity
import net.sipconsult.sipposorder.SharedViewModel
import net.sipconsult.sipposorder.data.models.CartItem
import net.sipconsult.sipposorder.databinding.HomeFragmentBinding
import net.sipconsult.sipposorder.ui.base.ScopedFragment
import net.sipconsult.sipposorder.ui.ordercart.OrderCartAdapter
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import java.text.DecimalFormat

class HomeFragment : ScopedFragment(), KodeinAware {

    override val kodein by closestKodein()
    private var _binding: HomeFragmentBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var sharedViewModel: SharedViewModel

    private lateinit var viewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = HomeFragmentBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        sharedViewModel = activity?.run {
            ViewModelProvider(this)[SharedViewModel::class.java]
        } ?: throw Exception("Invalid Activity")

        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        val navController = NavHostFragment.findNavController(this)

        sharedViewModel.authenticationState.observe(
            viewLifecycleOwner,
            Observer { authenticationState ->
                when (authenticationState) {
//                AuthenticationState.AUTHENTICATED -> showWelcomeMessage()
//                    AuthenticationState.UNAUTHENTICATED -> navController.navigate(R.id.loginFragment)
                }
            })

        binding.groupLoadingOrder.visibility = View.GONE
        val orderCartAdapter =
            OrderCartAdapter(
                ::onSubClick,
                ::onAddClick,
                ::onDeleteClick
            )
        binding.listOrderCart.adapter = orderCartAdapter
        binding.listOrderCart.addItemDecoration(
            DividerItemDecoration(
                binding.listOrderCart.context,
                DividerItemDecoration.VERTICAL
            )
        )
        sharedViewModel.cartItems.observe(viewLifecycleOwner, Observer {
            if (it.isNullOrEmpty()) {
                binding.listOrderCart.visibility = View.INVISIBLE
                binding.groupCart.visibility = View.VISIBLE
                binding.buttonPayment.isEnabled = false
                binding.buttonClearCart.isEnabled = false
            } else {
                binding.listOrderCart.visibility = View.VISIBLE
                binding.groupCart.visibility = View.INVISIBLE
                binding.buttonPayment.isEnabled = true
                binding.buttonClearCart.isEnabled = true
            }
            orderCartAdapter.setCartItems(it.asReversed())
        })

        sharedViewModel.totalCartPrice.observe(viewLifecycleOwner, Observer { totalPrice ->

            totalPrice?.let {
                val total = DecimalFormat("0.00").format(it)
                binding.textTotalOrder.text = String.format("Total: Ghc %s", total)
//                displayTotalPrice(String.format("Total:GHC%s", total))
            }

        })

        sharedViewModel.orderResult.observe(
            viewLifecycleOwner,
            Observer { result ->
                result ?: return@Observer
                binding.groupLoadingOrder.visibility = View.GONE

                result.getContentIfNotHandled()?.let { transactionResult ->

                    transactionResult.error?.let {
                        showTransactionFailed(it)
                    }
                    transactionResult.success?.let {
                        sharedViewModel.resetTransaction()
                        result.hasBeenHandled()
//                        navigateToReceipt()
                        sharedViewModel.resetAll()

                    }
                }


            })

        binding.buttonClearCart.setOnClickListener {
            viewModel.removeALLCartItem()
//            binding.textProductFoundNotFound.text = ""
        }

        binding.buttonPayment.setOnClickListener {
            binding.groupLoadingOrder.visibility = View.VISIBLE
            sharedViewModel.generateReceiptNumber()
            postOrder()
//            binding.textProductFoundNotFound.text = ""
        }
    }

    private fun postOrder() = launch {
        val result = sharedViewModel.postOrder.await()
        sharedViewModel.updateOrderResult(result)

    }

    private fun bindUI() = launch {
//        val salesAgents = viewModel.salesAgents.await()
//        if (view != null) {
//            salesAgents.observe(viewLifecycleOwner, Observer { salesAgents ->
//                if (salesAgents == null) return@Observer
//                groupSalesAgentLoading.visibility = View.GONE
//                setupSalesAgentRecyclerView(salesAgents as ArrayList<SalesAgentsItem>)
//            })
//        }

    }

//    private fun setupSalesAgentRecyclerView(products: ArrayList<SalesAgentsItem>) {
//        val salesAgentListAdapter =
//            SalesAgentListAdapter(::onSalesAgentClick)
//        listSalesAgent.adapter = salesAgentListAdapter
//        listSalesAgent.addItemDecoration(
//            DividerItemDecoration(
//                listSalesAgent.context,
//                DividerItemDecoration.VERTICAL
//            )
//        )
//        salesAgentListAdapter.setSalesAgent(products)
//
//    }
//
//
//
//    fun displayName(name: String) {
//        if (woyouService == null) {
////            Toast.makeText(context, "Service not ready", Toast.LENGTH_SHORT).show()
//            return
//        }
//        try {
//            woyouService!!.sendLCDString(name, null)
//        } catch (e: RemoteException) {
//            e.printStackTrace()
//        }
//    }
//
//    private fun displayTotalPrice(totalPrice: String) {
//        if (woyouService == null) {
////            Toast.makeText(context, "Service not ready", Toast.LENGTH_SHORT).show()
//            return
//        }
//        try {
//            //woyouService.sendLCDCommand(2);
//            woyouService!!.sendLCDDoubleString(totalPrice, "", null)
//        } catch (e: RemoteException) {
//            e.printStackTrace()
//        }
//    }


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

    private fun onDeleteClick(cartItem: CartItem) {
        viewModel.removeCartItem(cartItem)
    }

    private fun onSubClick(cartItem: CartItem) {
        viewModel.decreaseCartItemQuantity(cartItem)
    }

    private fun onAddClick(cartItem: CartItem) {
        viewModel.increaseCartItemQuantity(cartItem)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}