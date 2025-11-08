package com.example.agrotech.presentation.advisorlist

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import com.example.agrotech.common.GlobalVariables
import com.example.agrotech.common.Resource
import com.example.agrotech.common.Routes
import com.example.agrotech.common.UIState
import com.example.agrotech.data.repository.advisor.AdvisorRepository
import com.example.agrotech.data.repository.profile.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AdvisorListViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val advisorRepository: AdvisorRepository
): ViewModel() {

    private val _state = mutableStateOf(UIState<List<AdvisorCard>>())
    val state: State<UIState<List<AdvisorCard>>> get() = _state

    private val _list = mutableStateOf<List<AdvisorCard>>(emptyList())
    val list: State<List<AdvisorCard>> get() = _list

    private val _search = mutableStateOf("")
    val search: State<String> get() = _search

    private val _filter = mutableStateOf("Nombre")
    val filter: State<String> get() = _filter

    fun getAdvisorList() {
        _state.value = UIState(isLoading = true)
        viewModelScope.launch {
            when (val result = profileRepository.getAdvisorList(GlobalVariables.TOKEN)) {
                is Resource.Success -> {
                    val profiles = result.data ?: run {
                        _state.value = UIState(message = "No se encontraron asesores")
                        return@launch
                    }
                    val advisorCards = profiles.map { profile ->
                        val advisorResult = advisorRepository.searchAdvisorByUserId(profile.userId, GlobalVariables.TOKEN)
                        val advisorId = (advisorResult as? Resource.Success)?.data?.id ?: 0
                        val rating = (advisorResult as? Resource.Success)?.data?.rating ?: 0.0

                        AdvisorCard(
                            id = advisorId,
                            name = "${profile.firstName} ${profile.lastName}",
                            occupation = profile.occupation,
                            rating = rating,
                            link = profile.photo
                        )
                    }
                    _state.value = UIState(data = advisorCards)
                    _list.value = advisorCards
                }
                is Resource.Error -> {
                    _state.value = UIState(message = "Hubo un error recuperando la lista de asesores")
                }
            }
        }
    }

    fun filterAdvisorList() {
        val filteredList = list.value.filter(filter.value)
        _state.value = UIState(data = filteredList)
    }

    fun onSearchChange(search: String) {
        _search.value = search
    }

    fun onFilterChange(filter: String) {
        _filter.value = filter
    }

    private fun List<AdvisorCard>.filter(filter: String): List<AdvisorCard> {
        return when (filter) {
            "Nombre" -> filterByName()
            "Ocupación" -> filterByOccupation()
            "Calificación" -> filterByRating()
            else -> this
        }
    }

    private fun List<AdvisorCard>.filterByName(): List<AdvisorCard> {
        return filter { it.name.contains(search.value, ignoreCase = true) }
    }


    private fun List<AdvisorCard>.filterByOccupation(): List<AdvisorCard> {
        return filter { it.occupation.contains(search.value, ignoreCase = true) }
    }

    private fun List<AdvisorCard>.filterByRating(): List<AdvisorCard> {
        return filter { it.rating >= (search.value.toDoubleOrNull() ?: 0.0) }
    }


}