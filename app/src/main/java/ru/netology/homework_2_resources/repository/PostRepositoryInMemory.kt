package ru.netology.homework_2_resources.repository

import android.content.Context
import android.util.Log
import androidx.core.content.edit
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.netology.homework_2_resources.dto.Post

class PostRepositoryInMemory(
    private val context: Context,
) : PostRepository {
    /* внутри объекта PostRepositoryInMemory будем хранить неизменяемую переменную класса Post,
    которую будем при срабатывании каждой функции изменения данных копировать и перезаписывать в
    новую переменную.
    Кроме того в нем будет обертка data MutableLiveData(post) над этой переменной. Она будет
    сообщать нам, когда в Post что-то изменится. Сообщать будет своим подписчикам. Подписчиками
    будут активити. */
    companion object { // константа доступная для всех классов внутри репозитория, составляет "компанию" этому классу PostRepositoryInMemory
        private const val POSTS_KEY = "POSTS_KEY"
        private const val FILE_NAME = "posts.json"
    }

    private var nextId = 0L

    /* переменные для работы с преференсами, файлами и JSON */
    private val prefs = context.getSharedPreferences("posts", Context.MODE_PRIVATE)
    private val gson = Gson()
    private val type = TypeToken.getParameterized(List::class.java, Post::class.java).type
    private var posts : List<Post> = readPosts() // при каждой записи в переменную Post теперь будет вызываться этот set(value) и синхронизироваться с преференсом
        set(value) {
            field = value
            sync()
        }
    private val data = MutableLiveData(posts) // обертка над другим объектом, которая оповещает, если что-то изменилось внутри переданного в нее объекта

    /* при создании объекта будет запускаться метод (чтения из преференсов): */
    /*init {
        readPosts() // сам метод описан ниже
    }*/
    /* Закомментировари это, т.к. заполняем посты выше сразу в момент создания private var posts ...*/

    override fun getData(): LiveData<List<Post>> = data

    override fun likeById(id: Long) {
        Log.i("pvl_info", "click on like")
        posts = posts.map { post -> /* операция map позволяет создать новую коллекцию на основе существующей, см. Sequences */
            if (post.id == id) {
                post.copy(likedByMe = !post.likedByMe,
                            likes = if (post.likedByMe) post.likes-1 else post.likes+1) /*!*/
            }
            else {
                post
            }
        }
        data.value = posts
    }

    override fun share(id: Long) {
        Log.i("pvl_info", "click on share")
        posts = posts.map {post ->
            if (post.id == id) {
                post.copy(shares = post.shares + 1)
            }
            else {
                post
            }
        }
        data.value = posts
    }

    override fun removeById(id: Long) {
        posts = posts.filter { /* удаляем с соответствующим id */
            it.id != id
        }
        data.value = posts /* сохраняем в переменную в памяти */
    }

    override fun save(post: Post) {
        /* проверим, является ли это созданием нового поста (id=0) или редактированием старого */
        if (post.id == 0L) {
            posts = listOf(
                post.copy(
                    id = (posts.firstOrNull()?.id ?: 0L) + 1, // ++nextId
                    author = "Me",
                    /*content = "",*/ // будет подставлен в классе PostViewModel в changeContent() - вызывается в MainActivity
                    published = "Now",
                    likedByMe = false,
                    likes = 0,
                    shares = 0,
                    views = 0,
                    videoLink = ""
                )
            ) + posts
            data.value = posts
            return
        }

        /* а эта часть для обоих случаев (и редактирование и новый) : */
        posts = posts.map {
            if (it.id != post.id) it else it.copy(content = post.content)
        }
        data.value = posts
    }

    /* методы, обслуживающие хранение данных (преференсы) : */
    private fun sync() {
        /* через преференсы:
        prefs.edit {
            putString(POSTS_KEY, gson.toJson(posts))
        } */
        /* через файлы: */
        context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE).bufferedWriter().use {
            it.write(gson.toJson(posts))
        }
    }
    /* readPosts() - читаем посты из преференсов, и записываем в data.value
    Альтернативный вариант записи readPosts() ниже
    */
    /*
    private fun readPosts() {
        posts = prefs.getString(POSTS_KEY, null)?.let {
            gson.fromJson<List<Post>>(it, type)
        }.orEmpty()
        data.value = posts
    }
    */
    /* А в этом варианте записи - функция возвращает список постов, и мы используем ее выше, чтобы присвоить data.value
    private fun readPosts(): List<Post> = prefs.getString(POSTS_KEY, null)?.let {
            gson.fromJson<List<Post>>(it, type)
        }.orEmpty()
    */
    /* Вариант readPosts() через файлы: */
    private fun readPosts(): List<Post> {
        val file = context.filesDir.resolve(FILE_NAME)
        return if (file.exists()) {
            context.openFileInput(FILE_NAME).bufferedReader().use {
                gson.fromJson(it, type)
            }
        } else {
            emptyList()
        }
    }


}