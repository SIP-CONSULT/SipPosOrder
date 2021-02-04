package net.sipconsult.sipposorder.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import net.sipconsult.sipposorder.data.repository.location.LocationRepository

class SettingsViewModelFactory(
    private val locationRepository: LocationRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SettingsViewModel(locationRepository) as T
    }
}