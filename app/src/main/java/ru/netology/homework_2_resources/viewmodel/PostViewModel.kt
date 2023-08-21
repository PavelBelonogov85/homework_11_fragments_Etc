package ru.netology.homework_2_resources.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.netology.homework_2_resources.dto.Post
import ru.netology.homework_2_resources.repository.PostRepository
import ru.netology.homework_2_resources.repository.PostRepositoryInMemory

private val empty = Post(
    id=0,
    author = "",
    content = "",
    published = "",
    likedByMe = false,
    videoLink = ""
)

class PostViewModel(application: Application): AndroidViewModel(application) {
    /* Тут создаем класс, наследующий от ViewModel() который будет "ловить" события изменения
    в данных */
    private val repository: PostRepository = PostRepositoryInMemory(application)

    val data: LiveData<List<Post>> = repository.getData()
    fun likeById(id: Long) = repository.likeById(id)
    fun share(id: Long) = repository.share(id)

    fun removeById(id:Long) = repository.removeById(id)

    val edited = MutableLiveData(empty)

    fun save() {
        edited.value?.let {
            repository.save(it)
        }
        edited.value = empty
    }

    fun edit(post: Post) {
        edited.value = post
    }

    /* "своя" кнопка, отменяющая редактирование: */
    fun editCancel() {
        edited.value = empty /* =post.copy() */
    }

    fun changeContent(content: String) {
        edited.value?.let { post ->
            if (content!=post.content) {
                edited.value = post.copy(content=content)
            }
        }
    }
}