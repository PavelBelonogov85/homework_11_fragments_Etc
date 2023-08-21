package ru.netology.homework_2_resources.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.AdapterView
/*import android.widget.ListAdapter*/
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.homework_2_resources.databinding.CardPostBinding
import ru.netology.homework_2_resources.dto.Post

/* даем алиас (альтернитивное имя) OnLikeClickListener */
/*typealias OnLikeClickListener = (Post) -> Unit
typealias OnShareClickListener = (Post) -> Unit
typealias OnRemoveListener = (Post) -> Unit*/
/* чтобы не создавать на каждый слушатель переменную и не тащить их все, используем другой подход - через интерфйс: */
interface PostListener {
    fun onLike(post: Post)
    fun onShare(post: Post) {}
    fun onRemove(post: Post)
    fun onEdit(post: Post)
    fun onPlayVideo(post: Post)
} /* и на все события создаем только одну переменную слушателя */

class PostAdapter( /* связывает набор данных с набором View. "Предоставляет" очередной View элементу данных с помощью ViewHolder */
    /*private val onLikeClicked: OnLikeClickListener, // private val onLikeClicked: (Post) -> Unit,
    private val onShareClicked: OnShareClickListener,
    private val onRemoveClicked: OnRemoveListener,
     */
    private val listener: PostListener // вот она - единая переменная для всех слушателей
) : ListAdapter<Post, PostViewHolder>(PostDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = CardPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        Log.d("PostAdapter", "Создался ViewHolder")
        return PostViewHolder(
            binding = binding,
            listener = listener,
        )
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        Log.d("PostAdapter", "произошла привязка ViewHolder к данным (onBindViewHolder) $position")
        holder.bind(getItem(position))
    }


}