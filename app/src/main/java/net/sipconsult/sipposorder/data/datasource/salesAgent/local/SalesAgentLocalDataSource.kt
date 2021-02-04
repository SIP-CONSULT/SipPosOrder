package net.sipconsult.sipposorder.data.datasource.salesAgent.local

import androidx.lifecycle.LiveData
import net.sipconsult.sipposorder.data.models.SalesAgentsItem

interface SalesAgentLocalDataSource {
    val salesAgents: LiveData<List<SalesAgentsItem>>

    fun salesAgent(salesAgentId: Int): SalesAgentsItem

    fun updateSalesAgent(salesAgents: List<SalesAgentsItem>)
}