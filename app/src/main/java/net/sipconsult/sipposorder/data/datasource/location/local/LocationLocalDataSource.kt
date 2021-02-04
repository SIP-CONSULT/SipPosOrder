package net.sipconsult.sipposorder.data.datasource.location.local

import androidx.lifecycle.LiveData
import net.sipconsult.sipposorder.data.models.LocationsItem

interface LocationLocalDataSource {
    val locations: LiveData<List<LocationsItem>>

    fun location(locationId: Int): LocationsItem

    fun updateLocations(locations: List<LocationsItem>)
}