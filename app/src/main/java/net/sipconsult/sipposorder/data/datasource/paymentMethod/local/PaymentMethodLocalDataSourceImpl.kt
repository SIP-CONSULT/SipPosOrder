package net.sipconsult.sipposorder.data.datasource.paymentMethod.local

import androidx.lifecycle.LiveData
import net.sipconsult.sipposorder.data.db.PaymentMethodDao
import net.sipconsult.sipposorder.data.models.PaymentMethodItem

class PaymentMethodLocalDataSourceImpl(private val paymentMethodDao: PaymentMethodDao) :
    PaymentMethodLocalDataSource {
    override val paymentMethods: LiveData<List<PaymentMethodItem>>
        get() = paymentMethodDao.paymentMethods

    override fun updatePaymentMethods(paymentMethods: List<PaymentMethodItem>) {
        paymentMethodDao.deleteAll()
        paymentMethodDao.upsertAll(paymentMethods)
    }
}