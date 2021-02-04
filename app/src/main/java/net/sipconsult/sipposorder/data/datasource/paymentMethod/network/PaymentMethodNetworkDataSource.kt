package net.sipconsult.sipposorder.data.datasource.paymentMethod.network

import androidx.lifecycle.LiveData
import net.sipconsult.sipposorder.data.network.response.PaymentMethods
import net.sipconsult.sipposorder.internal.Result

interface PaymentMethodNetworkDataSource {
    val downloadPaymentMethods: LiveData<PaymentMethods>

    suspend fun fetchPaymentMethods()

}