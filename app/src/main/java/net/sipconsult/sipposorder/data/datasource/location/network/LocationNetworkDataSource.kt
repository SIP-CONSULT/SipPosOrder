package net.sipconsult.sipposorder.data.datasource.location.network

import androidx.lifecycle.LiveData
import net.sipconsult.sipposorder.data.network.response.Locations

interface LocationNetworkDataSource {
    val downloadLocations: LiveData<Locations>

    suspend fun fetchLocations()
}