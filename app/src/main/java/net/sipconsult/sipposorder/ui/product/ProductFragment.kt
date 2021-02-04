package net.sipconsult.sipposorder.ui.product

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.launch
import net.sipconsult.sipposorder.data.models.ProductItem
import net.sipconsult.sipposorder.databinding.ProductFragmentBinding
import net.sipconsult.sipposorder.ui.base.ScopedFragment
import net.sipconsult.sipposorder.ui.product.ProductListAdapter
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import java.util.*

class ProductFragment : ScopedFragment(), KodeinAware {

    override val kodein by closestKodein()

    private var _binding: ProductFragmentBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val viewModelFactory: ProductViewModelFactory by instance()

    private lateinit var viewModel: ProductViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = ProductFragmentBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(ProductViewModel::class.java)

        bindUI()
    }


    private fun bindUI() = launch {
        val locations = viewModel.locations.await()
        if (view != null) {
            locations.observe(viewLifecycleOwner, Observer { lcs ->
                if (lcs == null) return@Observer
                val size = lcs.size
//            Toast.makeText(context, "Locations size $size",Toast.LENGTH_SHORT).show()

            })
        }
        val products = viewModel.products.await()
        if (view != null) {
            products.observe(viewLifecycleOwner, Observer { pdts ->
                if (pdts == null) return@Observer
                binding.groupLoadingProducts.visibility = View.GONE
                setupProductRecyclerView(pdts as ArrayList<ProductItem>)
                viewModel.productItems = pdts
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

//    private fun setupSearchView(productListAdapter: ProductListAdapter) {
//        searchProducts.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
//            androidx.appcompat.widget.SearchView.OnQueryTextListener {
//            override fun onQueryTextSubmit(query: String?): Boolean {
//                hideKeyboard()
//                return false
//            }
//
//            override fun onQueryTextChange(newText: String?): Boolean {
//                productListAdapter.filter.filter(newText)
//                return false
//            }
//
//        })
//    }

    private fun setupProductRecyclerView(products: ArrayList<ProductItem>) {
        val productRecyclerAdapter =
            ProductListAdapter(::onProductClick)
        binding.listProducts.adapter = productRecyclerAdapter
        productRecyclerAdapter.setProducts(products)
//        setupSearchView(productRecyclerAdapter)
    }

    private fun onProductClick(product: ProductItem) {
        viewModel.addCartItem(product)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}