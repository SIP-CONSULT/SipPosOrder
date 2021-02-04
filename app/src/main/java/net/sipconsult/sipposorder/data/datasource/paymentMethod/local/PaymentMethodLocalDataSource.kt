package net.sipconsult.sipposorder.data.datasource.paymentMethod.local

import androidx.lifecycle.LiveData
import net.sipconsult.sipposorder.data.models.PaymentMethodItem

interface PaymentMethodLocalDataSource {

    val paymentMethods: LiveData<List<PaymentMethodItem>>

    fun updatePaymentMethods(paymentMethods: List<PaymentMethodItem>)
}