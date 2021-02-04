package net.sipconsult.sipposorder.data.datasource.paymentMethod.network

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import net.sipconsult.sipposorder.data.network.SipShopApiService
import net.sipconsult.sipposorder.data.network.response.PaymentMethods
import net.sipconsult.sipposorder.internal.NoConnectivityException
import net.sipconsult.sipposorder.internal.Result
import java.io.IOException

class PaymentMethodNetworkDataSourceImpl(private val sipShopApiService: SipShopApiService) :
    PaymentMethodNetworkDataSource {

    private val _downloadPaymentMethods = MutableLiveData<PaymentMethods>()

    override val downloadPaymentMethods: LiveData<PaymentMethods>
        get() = _downloadPaymentMethods

    override suspend fun fetchPaymentMethods() {
        try {
            val fetchedPaymentMethods = sipShopApiService.getPaymentMethodsAsync()
            _downloadPaymentMethods.postValue(fetchedPaymentMethods)

        } catch (e: NoConnectivityException) {
            Log.d(TAG, "fetchProducts: No internet Connection ", e)
        }
    }

    companion object {
        private const val TAG: String = "PaymentMethodNetDataSrc"
    }
}