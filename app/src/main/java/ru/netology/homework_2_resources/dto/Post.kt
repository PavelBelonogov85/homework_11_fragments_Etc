package ru.netology.homework_2_resources.dto

data class Post(
    val id: Long,
    val author: String,
    val content: String,
    val published: String,
    val likes: Long = 0,
    val likedByMe: Boolean = false,
    val shares: Long = 0,
    val views: Long = 0,
    val videoLink: String
)