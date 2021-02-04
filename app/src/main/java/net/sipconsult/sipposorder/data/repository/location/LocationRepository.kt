package net.sipconsult.sipposorder.data.repository.location

import androidx.lifecycle.LiveData
import net.sipconsult.sipposorder.data.models.LocationsItem

interface LocationRepository {

    suspend fun getLocations(): LiveData<List<LocationsItem>>
    fun getLocation(locationId: Int): LocationsItem
    fun getLocationsLocal(): LiveData<List<LocationsItem>>
}