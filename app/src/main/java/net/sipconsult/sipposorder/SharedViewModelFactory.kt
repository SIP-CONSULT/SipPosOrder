package net.sipconsult.sipposorder

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import net.sipconsult.sipposorder.data.provider.LocationProvider
import net.sipconsult.sipposorder.data.provider.PosNumberProvider
import net.sipconsult.sipposorder.data.repository.location.LocationRepository
import net.sipconsult.sipposorder.data.repository.order.OrderRepository
import net.sipconsult.sipposorder.data.repository.user.UserRepository

class SharedViewModelFactory(
    private val userRepository: UserRepository,
    private val orderRepository: OrderRepository,
    private val locationProvider: LocationProvider,
    private val posNumberProvider: PosNumberProvider,
    private val locationRepository: LocationRepository,
    val context: Context
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SharedViewModel(
            userRepository,
            orderRepository,
            locationProvider,
            posNumberProvider,
            locationRepository,
            context
        ) as T
    }
}