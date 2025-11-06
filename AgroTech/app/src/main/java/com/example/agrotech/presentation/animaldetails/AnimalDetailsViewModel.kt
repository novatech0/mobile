package com.example.agrotech.presentation.animaldetails

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.agrotech.common.GlobalVariables
import com.example.agrotech.common.Resource
import com.example.agrotech.common.UIState
import com.example.agrotech.data.repository.animal.AnimalRepository
import com.example.agrotech.domain.animal.Animal
import kotlinx.coroutines.launch

class AnimalDetailsViewModel(
    private val navController: NavController,
    private val animalRepository: AnimalRepository,
): ViewModel() {
    private val _state = mutableStateOf(UIState<Animal>())
    val state: UIState<Animal> get() = _state.value

    private val _expanded = mutableStateOf(false)
    val expanded: State<Boolean> get() = _expanded

    private val _name = mutableStateOf("")
    val name: String get() = _name.value

    private val _age = mutableIntStateOf(0)
    val age: Int get() = _age.intValue

    private val _species = mutableStateOf("")
    val species: String get() = _species.value

    private val _breed = mutableStateOf("")
    val breed: String get() = _breed.value

    private val _gender = mutableStateOf(true)
    val gender: Boolean get() = _gender.value

    private val _weight = mutableFloatStateOf(0f)
    val weight: Float get() = _weight.floatValue

    private val _health = mutableStateOf("")
    val health: String get() = _health.value

    private val _genderExpanded = mutableStateOf(false)
    val genderExpanded: State<Boolean> get() = _genderExpanded

    private val _healthStatusExpanded = mutableStateOf(false)
    val healthStatusExpanded: State<Boolean> get() = _healthStatusExpanded

    fun setExpanded(value: Boolean) {
        _expanded.value = value
    }

    fun goBack() {
        navController.popBackStack()
    }

    fun getAnimal(animalId: Long) {
        _state.value= UIState(isLoading = true)
        viewModelScope.launch {
            val result = animalRepository.getAnimalById(GlobalVariables.TOKEN, animalId)
            if (result is Resource.Success) {
                val animal = result.data
                _state.value = UIState(data = animal)
                _name.value = animal?.name ?: ""
                _age.intValue = animal?.age ?: 0
                _species.value = animal?.species ?: ""
                _breed.value = animal?.breed ?: ""
                _gender.value = animal?.gender ?: true
                _weight.floatValue = animal?.weight ?: 0f
                _health.value = animal?.health ?: ""
            }
            else
                _state.value = UIState(message = "Error al obtener el animal")
        }
    }

    fun updateAnimal(animalId: Long) {
        if (!validateData()) return
        _state.value = UIState(isLoading = true)
        viewModelScope.launch {
            val updatedAnimal = Animal(
                id = animalId,
                enclosureId = 0, // No se necesita actualizar el ID del recinto
                name = _name.value,
                age = _age.intValue,
                species = _species.value,
                breed = _breed.value,
                gender = _gender.value,
                weight = _weight.floatValue,
                health = _health.value
            )
            when (animalRepository.updateAnimal(GlobalVariables.TOKEN, updatedAnimal)) {
                is Resource.Success -> {
                    _state.value = UIState(data = updatedAnimal)
                }
                is Resource.Error -> {
                    _state.value = UIState(message = "Error al actualizar el animal")
                }
            }
        }
    }

    fun deleteAnimal(animalId: Long) {
        _state.value = UIState(isLoading = true)
        viewModelScope.launch {
            animalRepository.deleteAnimal(GlobalVariables.TOKEN, animalId)
            _state.value = UIState(message = "Animal eliminado")
            navController.popBackStack()
        }
    }

    fun setName(name: String) { _name.value = name }
    fun setAge(age: Int) { _age.intValue = age }
    fun setSpecies(species: String) { _species.value = species }
    fun setBreed(breed: String) { _breed.value = breed }
    fun setGender(gender: Boolean) { _gender.value = gender }
    fun setWeight(weight: Float) { _weight.floatValue = weight }
    fun setHealth(health: String) { _health.value = health }

    private fun validateData(): Boolean {
        return _name.value.isNotEmpty() &&
                _age.intValue > 0 &&
                _species.value.isNotEmpty() &&
                _breed.value.isNotEmpty() &&
                _weight.floatValue > 0f &&
                _health.value.isNotEmpty()
    }

    fun setGenderExpanded(isExpanded: Boolean) {
        _genderExpanded.value = isExpanded
    }

    fun setHealthStatusExpanded(isExpanded: Boolean) {
        _healthStatusExpanded.value = isExpanded
    }
}
