package net.sipconsult.sipposorder

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import net.sipconsult.sipposorder.data.models.*
import net.sipconsult.sipposorder.data.network.response.OrderResponse
import net.sipconsult.sipposorder.data.provider.LocationProvider
import net.sipconsult.sipposorder.data.provider.PosNumberProvider
import net.sipconsult.sipposorder.data.repository.location.LocationRepository
import net.sipconsult.sipposorder.data.repository.order.OrderRepository
import net.sipconsult.sipposorder.data.repository.orderCart.OrderCartRepository
import net.sipconsult.sipposorder.data.repository.user.UserRepository
import net.sipconsult.sipposorder.internal.Event
import net.sipconsult.sipposorder.internal.lazyDeferred
import net.sipconsult.sipposorder.ui.login.AuthenticationState
import net.sipconsult.sipposorder.ui.orders.OrdersResult
import java.text.DecimalFormat
import net.sipconsult.sipposorder.internal.Result
import net.sipconsult.sipposorder.ui.orders.TransactionResult
import java.text.SimpleDateFormat
import java.util.*

class SharedViewModel(
    private val userRepository: UserRepository,
    private val orderRepository: OrderRepository,
    private val locationProvider: LocationProvider,
    private val posNumberProvider: PosNumberProvider,
    private val locationRepository: LocationRepository,
    val context: Context
) : ViewModel() {

    private val sharedPref: SharedPreferences =
        context.getSharedPreferences("SipPosLogin", Context.MODE_PRIVATE)

    private val editor: SharedPreferences.Editor = sharedPref.edit()

    //    lateinit var user: LoggedInUser
    private val locationId: Int = locationProvider.getLocation()

    var transactionType: Int = 1
    var salesTransactionId: Int = 0
    private var dateNow = Date()
    private var orderNumber: String = ""
    private var total: Double = 0.0
    private val decimalFormater = DecimalFormat("0.00")
    private var username: String = ""
    var email: String = ""


    private var isOrderNumberGenerated: Boolean = false
    var isPrintReceipt: Boolean = false

    private val _authenticationState = MutableLiveData<AuthenticationState>()
    val authenticationState: LiveData<AuthenticationState> = _authenticationState

    private val _user = MutableLiveData<LoggedInUser>()
    val user: LiveData<LoggedInUser> = _user

    var cartItems: LiveData<MutableList<CartItem>> = OrderCartRepository.cartItems
    var orderCartItems: LiveData<MutableList<CartItem>> = OrderCartRepository.orderCartItems
    val location: LocationsItem = locationRepository.getLocation(locationId)

    val totalCartPrice: LiveData<Double> = OrderCartRepository.totalPrice

    private var _totalPrice = MutableLiveData<Double>()
    val totalPrice: LiveData<Double> = _totalPrice

    private var _totalDiscountPrice = MutableLiveData<Double>()
    var totalDiscountPrice = _totalDiscountPrice

    private val _salesAgent = MutableLiveData<SalesAgentsItem>()
    val salesAgent: LiveData<SalesAgentsItem> = _salesAgent

    private val _orderResult = MutableLiveData<Event<TransactionResult>>()
    val orderResult: LiveData<Event<TransactionResult>> = _orderResult

    var totalAmount: Double = 0.0

    private val _change = MutableLiveData<String>()
    val change: LiveData<String> = _change

    init {

        if (userRepository.isLoggedIn())
        // In this example, the user is always unauthenticated when MainActivity is launched
            _authenticationState.value = AuthenticationState.AUTHENTICATED
        else
            _authenticationState.value = AuthenticationState.UNAUTHENTICATED

        if (userRepository.isLoggedIn()) {
//            userRepository.loggedInUser.observeForever {
//                _user.value = it
//                username = it.displayName
//                email = it.email
//            }
        }

        _totalPrice.postValue(OrderCartRepository.totalCartPrice)
    }

    fun setTotalPrice() {
        if (transactionType == 1) {
            _totalPrice.postValue(OrderCartRepository.totalCartPrice)
        } else if (transactionType == 2) {
            _totalPrice.postValue(OrderCartRepository.refundTotalCartPrice)
        }
    }

//    fun setRefundTotalPrice() {
//        _refundTotalPrice.postValue(ShoppingCartRepository.refundTotalCartPrice)
//    }

    fun logout() {
        userRepository.logout()
        _authenticationState.postValue(AuthenticationState.UNAUTHENTICATED)
    }

    fun authenticate() {
        _authenticationState.value = AuthenticationState.AUTHENTICATED
    }


    private fun compareToDay(date1: Date?, date2: Date?): Int {
        if (date1 == null || date2 == null) {
            return 0
        }
        val sdf = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
        return sdf.format(date1).compareTo(sdf.format(date2))
    }

    private val generateOrderNumber: String
        get() {

            val dateNow = Date(System.currentTimeMillis())

            val millis = dateNow.time
            val posNumber = posNumberProvider.getPOSNumber()

            val currentDate: Long = sharedPref.getLong(KEY_CURRENT_DATE, 0L)
            var currentIndex: Long = sharedPref.getLong(KEY_CURRENT_INDEX, -1L)
            if (currentDate == 0L)
                editor.putLong(KEY_CURRENT_DATE, millis).apply()

            val myCurrentDate = Date(sharedPref.getLong(KEY_CURRENT_DATE, 0))

            val formatReceiptNumber = SimpleDateFormat("yyMMdd", Locale.getDefault())
            val dateToStr = formatReceiptNumber.format(dateNow)

//            val userInitial: String = user.value!!.abbrv

            if (compareToDay(dateNow, myCurrentDate) == 0) {
                if (currentIndex == -1L) {
                    editor.putLong(KEY_CURRENT_INDEX, 1).apply()
                }
                currentIndex = sharedPref.getLong(KEY_CURRENT_INDEX, 0L)

                orderNumber =
                    String.format("%s%o%s", dateToStr, currentIndex, posNumber)

                val newIndex = currentIndex + 1
                editor.putLong(KEY_CURRENT_INDEX, newIndex).apply()

            } else {

                editor.putLong(KEY_CURRENT_DATE, millis).apply()
                if (currentIndex == -1L) {
                    editor.putLong(KEY_CURRENT_INDEX, 1).apply()
                }

                editor.putLong(KEY_CURRENT_INDEX, 1).apply()
                currentIndex = sharedPref.getLong(KEY_CURRENT_INDEX, 0L)

                orderNumber =
                    String.format("%s%o%s", dateToStr, currentIndex, posNumber)

                val newIndex = currentIndex + 1
                editor.putLong(KEY_CURRENT_INDEX, newIndex).apply()
            }

            return if (transactionType == 1) {
                orderNumber
            } else {
                orderNumber + "R"
            }
        }

    fun generateReceiptNumber() {
        if (!isOrderNumberGenerated) {
            orderNumber = generateOrderNumber
            isOrderNumberGenerated = true
        }

    }

    val postOrder by lazyDeferred {

        val locationP = locationRepository.getLocation(locationId)
        val items = arrayListOf<OrderItemPostBody>()

        for (item in cartItems.value!!) {
            val item = OrderItemPostBody(
                itemId = item.product.id,
                quantity = item.quantity
            )
            items.add(item)
        }

        val body = OrderPostBody(
            locationId = locationP.id,
            salesAgentId = 1,
            orderNumber = orderNumber,
            orderItems = items
        )

        orderRepository.postOrder(body)

    }


    fun updateOrderResult(result: Result<OrderResponse>) {

        _orderResult.value = if (result is Result.Success) {
            Event(TransactionResult(success = result.data.successful))
        } else {
            Event(TransactionResult(error = R.string.transaction_failed))
        }

    }

    fun resetAll() {
        OrderCartRepository.removeALLCartItem()
        OrderCartRepository.removeALLOrderCartItem()
        _salesAgent.postValue(null)
//        _transactionResult.postValue(null)
        totalAmount = 0.0
        isOrderNumberGenerated = false
        isPrintReceipt = false
    }

    fun resetPaymentMethodAndDiscount() {
        _salesAgent.postValue(null)
    }

    fun resetTransaction() {
//        _transactionResult.postValue(null)
    }

    companion object {
        private const val KEY_CURRENT_DATE = "current_date"
        private const val KEY_CURRENT_INDEX = "current_index"
    }

}