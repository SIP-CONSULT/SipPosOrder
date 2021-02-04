package net.sipconsult.sipposorder.data.repository.salesAgent

import androidx.lifecycle.LiveData
import net.sipconsult.sipposorder.data.models.SalesAgentsItem

interface SalesAgentRepository {
    suspend fun getSalesAgents(): LiveData<List<SalesAgentsItem>>
    fun getSalesAgent(salesAgentId: Int): SalesAgentsItem
    fun getSalesAgentsLocal(): LiveData<List<SalesAgentsItem>>
}