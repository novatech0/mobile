package com.example.agrotech.presentation.newpost

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.agrotech.common.GlobalVariables
import com.example.agrotech.common.Resource
import com.example.agrotech.common.UIState
import com.example.agrotech.data.repository.advisor.AdvisorRepository
import com.example.agrotech.data.repository.post.PostRepository
import com.example.agrotech.domain.post.CreatePost
import kotlinx.coroutines.launch
import java.io.File

class NewPostViewModel(private val navController: NavController,
                       private val postRepository: PostRepository,
                       private val advisorRepository: AdvisorRepository,
)
    : ViewModel()
{
    private val _title = mutableStateOf("")
    val title: State<String> get() = _title

    private val _description = mutableStateOf("")
    val description: State<String> get() = _description

    private val _image = mutableStateOf<File?>(null)
    val image: State<File?> get() = _image

    private val _state = mutableStateOf(UIState<Unit>())
    val state: State<UIState<Unit>> get() = _state

    fun goBack() {
        navController.popBackStack()
    }

    fun createPost() {
        _state.value = UIState(isLoading = true)
        viewModelScope.launch {
            val advisor = advisorRepository.searchAdvisorByUserId(GlobalVariables.USER_ID, GlobalVariables.TOKEN)
            if (advisor is Resource.Error) {
                _state.value = UIState(message = "Error al obtener el asesor")
                return@launch
            }
            if (advisor.data == null) {
                _state.value = UIState(message = "Asesor no encontrado")
                return@launch
            }

            if (_title.value.isEmpty() || _description.value.isEmpty() || _image.value == null) {
                _state.value = UIState(message = "Todos los campos son obligatorios")
            }

            val post = CreatePost(
                advisorId = advisor.data.id,
                title = _title.value,
                description = _description.value,
                image = _image.value!!,

            )

            when (postRepository.createPost(GlobalVariables.TOKEN, post)) {
                is Resource.Success -> {
                    _state.value = UIState(isLoading = false)
                    navController.popBackStack()
                }
                is Resource.Error -> {
                    _state.value = UIState(message = "Error al crear la publicación")
                }
            }
        }
    }

    fun setTitle(value: String) {
        _title.value = value
    }

    fun setDescription(value: String) {
        _description.value = value
    }

    fun setImage(value: File) {
        _image.value = value
    }
}