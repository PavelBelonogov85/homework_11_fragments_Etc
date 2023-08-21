package ru.netology.homework_2_resources.adapter

import android.content.Intent
import android.widget.PopupMenu
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import ru.netology.homework_2_resources.R
import ru.netology.homework_2_resources.databinding.CardPostBinding
import ru.netology.homework_2_resources.dto.Post
import ru.netology.homework_2_resources.utils.StringsVisability

class PostViewHolder ( /* "держит" ссылку на конкретный view ; определяет КАК ее заполнять по входящим данным ; привязывает к событиям на элементах действия: sharesIcon.setOnClickListener {onShareClicked()}  */
    private val binding: CardPostBinding,
    private val listener: PostListener
        ): ViewHolder(binding.root) { /* ссылка на конкретную вьюшку, он ее хранит и ее можно из него забрать при необходимости */

    fun bind(post: Post) {
        with(binding) {
            /* все изменения визуализации (тексты, картинки...) переезжают сюда!
               а все изменения скрытых данных (счетчики и др.) будут в функциях viewModel */

            // какие объекты верстки доступны по ID можно проверить:
            // написав "binding." по всплывающей подсказке
            // или посмотреть класс в "\project_name\app\build\generated\data_binding_base_class_source_out\debug\out\ru\netology\homework_2_resources\databinding\ActivityMainBinding.java"
            author.text = post.author
            published.text = post.published
            content.text = post.content
            avatar.setImageResource(R.drawable.ic_launcher_foreground)
            viewsText.text = StringsVisability.getCoolNumeralString(post.views)

            // ! binding самопроизвольно меняет id элементов из snake_case в camelCase !
            /*if (post.likedByMe) {
                likesIcon.setImageResource(R.drawable.ic_liked_24)
            } else {
                likesIcon.setImageResource(R.drawable.baseline_favorite_border_24)
            }*/
            // лучше всегда менять все поля, чтобы не получить по карусели предыдущую версию view, поэтому
            // лучше писать вот так:
            likesIcon.isChecked = post.likedByMe
            likesIcon.text = StringsVisability.getCoolNumeralString(post.likes)
            sharesIcon.text = StringsVisability.getCoolNumeralString(post.shares)

            binding.likesIcon.setOnClickListener {
                //viewModel.likeById(post.id)
                listener.onLike(post)
            }
            binding.sharesIcon.setOnClickListener {
                //viewModel.share(post.id)
                listener.onShare(post)
            }

            /* работаем с "видео": */
            if (post.videoLink!="") {
                binding.video.setImageResource(R.drawable.dummy_img) // не правильно брать картинку ДАННЫХ из drawable
                videoButton.setImageResource(R.drawable.youtube_btn)
            } else {
                binding.video.setImageResource(0)
                videoButton.setImageResource(0)
            }
            video.setOnClickListener {
                listener.onPlayVideo(post) /* см. https://developer.android.com/guide/components/intents-common#Music */
            }

            /* создаем меню: */
            menu.setOnClickListener {
                PopupMenu(it.context, it).apply {
                    inflate(R.menu.post_options) /* наполняем его из ресурсов */
                    setOnMenuItemClickListener {item ->
                        when (item.itemId) {
                            R.id.remove -> {
                                listener.onRemove(post)
                                binding.menu.isChecked = false /* развлекаемся с раскраской кнопки меню от признака Checked */
                                true
                            }
                            R.id.edit -> {
                                listener.onEdit(post)
                                binding.menu.isChecked = false
                                true
                            }
                            else -> false
                        }
                    }
                    setOnDismissListener {
                        binding.menu.isChecked = false
                        true}

                }.show()
                binding.menu.isChecked = true
            }
        }

    }
}