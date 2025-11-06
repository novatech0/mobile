package com.example.agrotech.presentation.animallist

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.agrotech.common.GlobalVariables
import com.example.agrotech.common.Resource
import com.example.agrotech.common.Routes
import com.example.agrotech.common.UIState
import com.example.agrotech.data.repository.animal.AnimalRepository
import com.example.agrotech.data.repository.enclosure.EnclosureRepository
import com.example.agrotech.domain.animal.Animal
import com.example.agrotech.domain.enclosure.Enclosure
import kotlinx.coroutines.launch

class AnimalListViewModel(
    private val navController: NavController,
    private val animalRepository: AnimalRepository,
    private val enclosureRepository: EnclosureRepository
): ViewModel() {
    private val _state = mutableStateOf(UIState<List<Animal>>())
    val state: UIState<List<Animal>> get() = _state.value

    private val _enclosure = mutableStateOf<UIState<Enclosure>>(UIState())
    val enclosure: UIState<Enclosure> get() = _enclosure.value

    private val _showAnimalDialog = mutableStateOf(false)
    val showAnimalDialog: Boolean get() = _showAnimalDialog.value

    private val _showEnclosureDialog = mutableStateOf(false)
    val showEnclosureDialog: Boolean get() = _showEnclosureDialog.value

    private val _animalName = mutableStateOf("")
    val animalName: String get() = _animalName.value

    private val _animalAge = mutableIntStateOf(0)
    val animalAge: Int get() = _animalAge.intValue

    private val _animalSpecies = mutableStateOf("")
    val animalSpecies: String get() = _animalSpecies.value

    private val _animalBreed = mutableStateOf("")
    val animalBreed: String get() = _animalBreed.value

    private val _animalGender = mutableStateOf(true)
    val animalGender: Boolean get() = _animalGender.value

    private val _animalWeight = mutableFloatStateOf(0f)
    val animalWeight: Float get() = _animalWeight.floatValue

    private val _animalHealthStatus = mutableStateOf("")
    val animalHealthStatus: String get() = _animalHealthStatus.value

    private val _enclosureName = mutableStateOf("")
    val enclosureName: String get() = _enclosureName.value

    private val _enclosureCapacity = mutableIntStateOf(0)
    val enclosureCapacity: Int get() = _enclosureCapacity.intValue

    private val _enclosureType = mutableStateOf("")
    val enclosureType: String get() = _enclosureType.value

    private val _genderExpanded = mutableStateOf(false)
    val genderExpanded: State<Boolean> get() = _genderExpanded

    private val _animalHealthExpanded = mutableStateOf(false)
    val animalHealthExpanded: State<Boolean> get() = _animalHealthExpanded

    private val _expanded = mutableStateOf(false)
    val expanded: State<Boolean> get() = _expanded

    fun getAnimals(enclosureId: Long) {
        _state.value = UIState(isLoading = true)
        viewModelScope.launch {
            val result = animalRepository.getAnimalsByEnclosureId(GlobalVariables.TOKEN, enclosureId)
            if (result is Resource.Success) {
                _state.value = UIState(data = result.data)
            } else {
                _state.value = UIState(message = "Error obteniendo los animales")
            }
        }
    }

    fun getEnclosure(enclosureId: Long) {
        _enclosure.value = UIState(isLoading = true)
        viewModelScope.launch {
            val result = enclosureRepository.getEnclosureById(GlobalVariables.TOKEN, enclosureId)
            if (result is Resource.Success) {
                _enclosure.value = UIState(data = result.data)
                _enclosureName.value = result.data?.name ?: ""
                _enclosureCapacity.intValue = result.data?.capacity ?: 0
                _enclosureType.value = result.data?.type ?: ""
            } else {
                _enclosure.value = UIState(message = "Error obteniendo el recinto")
            }
        }
    }

    fun editEnclosure(enclosureId: Long) {
        if (!validateEnclosure()) return
        viewModelScope.launch {
            enclosureRepository.updateEnclosure(
                GlobalVariables.TOKEN,
                Enclosure(
                    id = enclosureId,
                    farmerId = 0, // FarmerId is not needed for update
                    name = _enclosureName.value,
                    capacity = _enclosureCapacity.intValue,
                    type = _enclosureType.value
                )
            )
            clearFields()
            _showEnclosureDialog.value = false
            getEnclosure(enclosureId)
        }
    }

    fun createAnimal(enclosureId: Long) {
        if (!validateAnimal()) return
        viewModelScope.launch {
            val newAnimal = Animal(
                id = 0L,
                enclosureId = enclosureId,
                name = animalName,
                age = animalAge,
                species = animalSpecies,
                breed = animalBreed,
                gender = animalGender,
                weight = animalWeight,
                health = animalHealthStatus
            )
            animalRepository.createAnimal(GlobalVariables.TOKEN, newAnimal)
            clearFields()
            _showAnimalDialog.value = false
            getAnimals(enclosureId)
        }

    }

    fun goBack() {
        navController.popBackStack()
    }

    fun goToAnimalDetails(animalId: Long) {
        navController.navigate(Routes.AnimalDetail.route + "/$animalId")
    }

    fun setExpanded(value: Boolean) {
        _expanded.value = value
    }

    fun deleteEnclosure(enclosureId: Long) {
        _state.value = UIState(isLoading = true)
        viewModelScope.launch {
            enclosureRepository.deleteEnclosure(GlobalVariables.TOKEN, enclosureId)
            _expanded.value = false
            navController.popBackStack()
        }
    }

    fun setShowAnimalDialog(value: Boolean) {
        _showAnimalDialog.value = value
    }

    fun setShowEnclosureDialog(value: Boolean) {
        _showEnclosureDialog.value = value
    }

    fun setGenderExpanded(value: Boolean) {
        _genderExpanded.value = value
    }

    fun setAnimalHealthExpanded(value: Boolean) {
        _animalHealthExpanded.value = value
    }

    private fun clearFields() {
        _animalName.value = ""
        _animalAge.intValue = 0
        _animalSpecies.value = ""
        _animalBreed.value = ""
        _animalGender.value = true
        _animalWeight.floatValue = 0f
        _animalHealthStatus.value = ""
        _enclosureName.value = ""
        _enclosureCapacity.intValue = 0
        _enclosureType.value = ""
    }

    fun setAnimalName(name: String) { _animalName.value = name }
    fun setAnimalAge(age: Int) { _animalAge.intValue = age }
    fun setAnimalSpecies(species: String) { _animalSpecies.value = species }
    fun setAnimalBreed(breed: String) { _animalBreed.value = breed }
    fun setAnimalGender(gender: Boolean) { _animalGender.value = gender }
    fun setAnimalWeight(weight: Float) { _animalWeight.floatValue = weight }
    fun setAnimalHealthStatus(status: String) { _animalHealthStatus.value = status }
    fun setEnclosureName(name: String) { _enclosureName.value = name }
    fun setEnclosureCapacity(capacity: Int) { _enclosureCapacity.intValue = capacity }
    fun setEnclosureType(type: String) { _enclosureType.value = type }

    private fun validateAnimal(): Boolean {
        return animalName.isNotEmpty() && animalAge >= 0 && animalSpecies.isNotEmpty() &&
                animalBreed.isNotEmpty() && animalWeight > 0f && animalHealthStatus.isNotEmpty()
    }

    private fun validateEnclosure(): Boolean {
        return enclosureName.isNotEmpty() && enclosureCapacity > 0 && enclosureType.isNotEmpty()
    }
}
