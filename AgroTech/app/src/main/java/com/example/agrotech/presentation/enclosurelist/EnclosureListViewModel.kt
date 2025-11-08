package com.example.agrotech.presentation.enclosurelist

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.lifecycle.viewModelScope
import com.example.agrotech.common.GlobalVariables
import com.example.agrotech.common.Resource
import com.example.agrotech.common.Routes
import com.example.agrotech.common.UIState
import com.example.agrotech.data.repository.enclosure.EnclosureRepository
import com.example.agrotech.data.repository.farmer.FarmerRepository
import com.example.agrotech.domain.enclosure.Enclosure
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EnclosureListViewModel @Inject constructor(
    private val enclosureRepository: EnclosureRepository,
    private val farmerRepository: FarmerRepository
): ViewModel() {
    private val _state = mutableStateOf(UIState<List<Enclosure>>())
    val state: UIState<List<Enclosure>> get() = _state.value

    private val _showDialog = mutableStateOf(false)
    val showDialog: Boolean get() = _showDialog.value

    private val _name = mutableStateOf("")
    val name: String get() = _name.value

    private val _capacity = mutableIntStateOf(0)
    val capacity: Int get() = _capacity.intValue

    private val _type = mutableStateOf("")
    val type: String get() = _type.value

    fun getEnclosures() {
        _state.value = UIState(isLoading = true)
        viewModelScope.launch {
            val result = farmerRepository.searchFarmerByUserId(GlobalVariables.USER_ID, GlobalVariables.TOKEN)
            if (result is Resource.Success) {
                val farmer = result.data
                if (farmer != null) {
                    val enclosuresResult = enclosureRepository.getEnclosuresByFarmerId(GlobalVariables.TOKEN, farmer.id)
                    if (enclosuresResult is Resource.Success) {
                        _state.value = UIState(data = enclosuresResult.data)
                    } else {
                        _state.value = UIState(message = "Error fetching enclosures")
                    }
                } else {
                    _state.value = UIState(message = "Farmer not found")
                }
            } else {
                _state.value = UIState(message = "Error fetching farmer information")
            }
        }
    }

    fun createEnclosure() {
        if (!validateEnclosure()) return
        viewModelScope.launch {
            val farmerResult = farmerRepository.searchFarmerByUserId(GlobalVariables.USER_ID, GlobalVariables.TOKEN)
            if (farmerResult is Resource.Success && farmerResult.data != null) {
                val newEnclosure = Enclosure(
                    id = 0L,
                    farmerId = farmerResult.data.id,
                    name = name,
                    capacity = capacity,
                    type = type
                )
                enclosureRepository.createEnclosure(GlobalVariables.TOKEN, newEnclosure)
                clearFields()
                hideDialog()
                getEnclosures()
            }
        }
    }

    fun showDialog() {
        _showDialog.value = true
    }

    fun hideDialog() {
        _showDialog.value = false
    }

    fun setName(name: String) {
        _name.value = name
    }

    fun setCapacity(capacity: Int) {
        _capacity.intValue = capacity
    }

    fun setType(type: String) {
        _type.value = type
    }

    private fun validateEnclosure(): Boolean {
        return name.isNotEmpty() && capacity > 0 && type.isNotEmpty()
    }

    private fun clearFields() {
        _name.value = ""
        _capacity.intValue = 0
        _type.value = ""
    }

}
