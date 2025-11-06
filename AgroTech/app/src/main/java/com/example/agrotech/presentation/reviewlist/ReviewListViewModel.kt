package com.example.agrotech.presentation.reviewlist

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.agrotech.common.GlobalVariables
import com.example.agrotech.common.Resource
import com.example.agrotech.common.UIState
import com.example.agrotech.data.repository.advisor.AdvisorRepository
import com.example.agrotech.data.repository.farmer.FarmerRepository
import com.example.agrotech.data.repository.profile.ProfileRepository
import com.example.agrotech.data.repository.appointment.ReviewRepository
import com.example.agrotech.presentation.advisorlist.AdvisorCard
import kotlinx.coroutines.launch

class ReviewListViewModel(private val navController: NavController, private val reviewRepository: ReviewRepository,
                          private val profileRepository: ProfileRepository,
                          private val farmerRepository: FarmerRepository,
                          private val advisorRepository: AdvisorRepository
): ViewModel() {
    private val _state = mutableStateOf(UIState<List<ReviewCard>>())
    val state: State<UIState<List<ReviewCard>>> get() = _state

    private val _advisorCard = mutableStateOf(UIState<AdvisorCard>())
    val advisorCard: State<UIState<AdvisorCard>> get() = _advisorCard

    fun goBack() {
        navController.popBackStack()
    }

    fun getAdvisorDetail(advisorId: Long) {
        _advisorCard.value = UIState(isLoading = true)
        viewModelScope.launch {
            val result = advisorRepository.searchAdvisorByAdvisorId(advisorId, GlobalVariables.TOKEN)
            if (result is Resource.Success) {
                val advisor = result.data ?: run {
                    _advisorCard.value = UIState(message = "No se encontró el asesor")
                    return@launch
                }
                val profileResult = profileRepository.searchProfile(advisor.userId, GlobalVariables.TOKEN)
                if (profileResult is Resource.Success) {
                    val profile = profileResult.data ?: run {
                        _advisorCard.value = UIState(message = "No se encontró el perfil del asesor")
                        return@launch
                    }
                    _advisorCard.value = UIState(
                        data = AdvisorCard(
                            id = advisor.id,
                            name = profile.firstName + ' ' + profile.lastName,
                            occupation = profile.occupation,
                            rating = advisor.rating,
                            link = profile.photo
                        )
                    )
                }
            } else {
                _advisorCard.value = UIState(message = "Error al intentar obtener el asesor")
            }
        }
    }

    fun getAdvisorReviewList(advisorId: Long) {
        _state.value = UIState(isLoading = true)
        viewModelScope.launch {
            val result = reviewRepository.getAdvisorReviewsList(advisorId, GlobalVariables.TOKEN)

            if (result is Resource.Success) {
                val reviews = result.data ?: run {
                    _state.value = UIState(message = "No se encontraron reseñas para este asesor")
                    return@launch
                }

                val reviewCards = reviews.map { review ->
                    val farmerResult = farmerRepository.searchFarmerByFarmerId(review.farmerId, GlobalVariables.TOKEN)
                    val farmerProfile = if (farmerResult is Resource.Success) {
                        val farmer = farmerResult.data
                        if (farmer != null) {
                            val profileResult = profileRepository.searchProfile(farmer.userId, GlobalVariables.TOKEN)
                            if (profileResult is Resource.Success) profileResult.data else null
                        } else null
                    } else null

                    val farmerName = farmerProfile?.let { "${it.firstName} ${it.lastName}" } ?: "Anónimo"
                    val farmerLink = farmerProfile?.photo ?: ""

                    ReviewCard(
                        id = review.id,
                        farmerName = farmerName,
                        comment = review.comment,
                        rating = review.rating,
                        farmerLink = farmerLink
                    )
                }
                _state.value = UIState(data = reviewCards)
            } else {
                _state.value = UIState(message = "Error al intentar obtener las reseñas del asesor")
            }
        }
    }


}