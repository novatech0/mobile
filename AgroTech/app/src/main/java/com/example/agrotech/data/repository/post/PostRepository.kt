package com.example.agrotech.data.repository.post

import com.example.agrotech.common.Resource
import com.example.agrotech.data.remote.post.PostService
import com.example.agrotech.data.remote.post.toPost
import com.example.agrotech.domain.post.CreatePost
import com.example.agrotech.domain.post.Post
import com.example.agrotech.domain.post.UpdatePost
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

class PostRepository @Inject constructor(private val postService: PostService) {

    suspend fun getPosts(token: String): Resource<List<Post>> = withContext(Dispatchers.IO) {
        if (token.isBlank()) {
            return@withContext Resource.Error(message = "Un token es requerido")
        }
        val bearerToken = "Bearer $token"
        val response = postService.getPosts(bearerToken)
        if (response.isSuccessful) {
            response.body()?.let { postsDto ->
                val posts = postsDto.map { it.toPost() }
                return@withContext Resource.Success(posts)
            }
            return@withContext Resource.Error(message = "Error al obtener publicaciones")
        }
        else {
            return@withContext Resource.Error(response.message())
        }
    }

    suspend fun getPostById(token: String, postId: Long): Resource<Post> = withContext(Dispatchers.IO) {
        if (token.isBlank()) {
            return@withContext Resource.Error(message = "Un token es requerido")
        }
        val bearerToken = "Bearer $token"
        val response = postService.getPost(bearerToken, postId)
        if (response.isSuccessful) {
            response.body()?.let { postDto ->
                val post = postDto.toPost()
                return@withContext Resource.Success(post)
            }
            return@withContext Resource.Error(message = "Error al obtener la publicación")
        }
        else {
            return@withContext Resource.Error(response.message())
        }
    }

    suspend fun getPostsByAdvisorId(token: String, advisorId: Long): Resource<List<Post>> = withContext(Dispatchers.IO) {
        if (token.isBlank()) {
            return@withContext Resource.Error(message = "Un token es requerido")
        }
        val bearerToken = "Bearer $token"
        val response = postService.getPostsByAdvisorId(bearerToken, advisorId)
        if (response.isSuccessful) {
            response.body()?.let { postsDto ->
                val posts = postsDto.map { it.toPost() }
                return@withContext Resource.Success(posts)
            }
            return@withContext Resource.Error(message = "Error al obtener publicaciones")
        }
        else {
            return@withContext Resource.Error(response.message())
        }
    }

    suspend fun updatePost(token: String, post: UpdatePost): Resource<Post> = withContext(Dispatchers.IO) {
        if (token.isBlank()) {
            return@withContext Resource.Error(message = "Un token es requerido")
        }

        val bearerToken = "Bearer $token"

        val title = post.title.toRequestBody()
        val description = post.description.toRequestBody()

        val imagePart = post.image?.let { file ->
            val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
            MultipartBody.Part.createFormData("image", file.name, requestFile)
        }

        val response = postService.updatePost(
            token = bearerToken,
            id = post.id,
            title = title,
            description = description,
            image = imagePart
        )

        if (response.isSuccessful) {
            response.body()?.let { postDto ->
                val updatedPost = postDto.toPost()
                return@withContext Resource.Success(updatedPost)
            }
            return@withContext Resource.Error(message = "No se pudo actualizar la publicación")
        }

        return@withContext Resource.Error(response.message())
    }


    suspend fun createPost(token: String, post: CreatePost): Resource<Post> = withContext(Dispatchers.IO) {
        if (token.isBlank()) return@withContext Resource.Error(message = "Un token es requerido")

        val bearerToken = "Bearer $token"

        val advisorId = post.advisorId.toString().toRequestBody()
        val title = post.title.toRequestBody()
        val description = post.description.toRequestBody()
        val image = post.image.asRequestBody("image/*".toMediaTypeOrNull())
        val imagePart = MultipartBody.Part.createFormData("image", post.image.name, image)

        val response = postService.createPost(bearerToken, advisorId, title, description, imagePart)
        if (response.isSuccessful) {
            response.body()?.let { postDto ->
                val postCreated = postDto.toPost()
                return@withContext Resource.Success(postCreated)
            }
            return@withContext Resource.Error(message = "No se pudo crear la publicación")
        }
        return@withContext Resource.Error(response.message())
    }

    suspend fun deletePost(token: String, postId: Long): Resource<Unit> = withContext(Dispatchers.IO) {
        if (token.isBlank()) {
            return@withContext Resource.Error(message = "Un token es requerido")
        }
        val bearerToken = "Bearer $token"
        val response = postService.deletePost(bearerToken, postId)
        if (response.isSuccessful) {
            return@withContext Resource.Success(Unit)
        }
        return@withContext Resource.Error(response.message())
    }
}