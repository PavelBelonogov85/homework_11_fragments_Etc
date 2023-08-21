package ru.netology.homework_2_resources.repository

import androidx.lifecycle.LiveData
import ru.netology.homework_2_resources.dto.Post

interface PostRepository {
    fun getData(): LiveData<List<Post>>
    fun likeById(id: Long)
    fun share(id: Long)
    fun removeById(id: Long)
    fun save(post: Post)
}