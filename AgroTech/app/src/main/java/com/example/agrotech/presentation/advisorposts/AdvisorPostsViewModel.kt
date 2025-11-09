package com.example.agrotech.presentation.advisorposts

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.agrotech.common.GlobalVariables
import com.example.agrotech.common.Resource
import com.example.agrotech.common.Routes
import com.example.agrotech.common.UIState
import com.example.agrotech.data.repository.advisor.AdvisorRepository
import com.example.agrotech.data.repository.post.PostRepository
import com.example.agrotech.domain.post.Post
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdvisorPostsViewModel @Inject constructor(
    private val postRepository: PostRepository,
    private val advisorRepository: AdvisorRepository
): ViewModel() {
    private val _state = mutableStateOf(UIState<List<Post>>())
    val state: State<UIState<List<Post>>> get() = _state

    fun getPosts() {
        _state.value = UIState(isLoading = true)
        viewModelScope.launch {
            val advisorId = advisorRepository.searchAdvisorByUserId(GlobalVariables.USER_ID, GlobalVariables.TOKEN).data?.id
                ?: run {
                    _state.value = UIState(message = "Error al recuperar datos del asesor")
                    return@launch
                }
            when (val result = postRepository.getPostsByAdvisorId(GlobalVariables.TOKEN, advisorId)) {
                is Resource.Success -> {
                    val posts = result.data ?: run {
                        _state.value = UIState(message = "No se encontraron publicaciones")
                        return@launch
                    }
                    _state.value = UIState(data = posts)
                }
                is Resource.Error -> {
                    _state.value = UIState(message = "Error al obtener tus publicaciones")
                }
            }
        }
    }
}