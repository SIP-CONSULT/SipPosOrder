package net.sipconsult.sipposorder.data.datasource.salesAgent.network

import androidx.lifecycle.LiveData
import net.sipconsult.sipposorder.data.network.response.SalesAgents

interface SalesAgentNetworkDataSource {

    val downloadSalesAgents: LiveData<SalesAgents>

    suspend fun fetchSalesAgents()
}