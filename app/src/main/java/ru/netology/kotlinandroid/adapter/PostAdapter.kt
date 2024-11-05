package ru.netology.kotlinandroid.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.widget.PopupMenu
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.kotlinandroid.R
import ru.netology.kotlinandroid.databinding.CardPostBinding
import ru.netology.kotlinandroid.dto.Post

interface OnInteractionListener {
    fun onLike(post: Post)
    fun onShare(post: Post)
    fun onRemove(post: Post)
    fun onEdit(post: Post)
    fun onClearEdit()
    fun onVideo(post: Post)
}

class PostAdapter(
    private val onInteractionListener: OnInteractionListener
) : ListAdapter<Post, PostViewHolder>(PostDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = CardPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding, onInteractionListener)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = getItem(position)
        holder.bind(post)
    }
}

class PostViewHolder(
    private val binding: CardPostBinding,
    private val onInteractionListener: OnInteractionListener
) : RecyclerView.ViewHolder(binding.root) {


    private fun formatValue(value: Int): String {
        return when {
            value >= 1_000_000 -> "${String.format("%.1f", value / 1_000_000.0)}M"
            value >= 10_000 -> "${value / 1000}K"
            value >= 1_100 -> "${String.format("%.1f", value / 1000.0)}K"
            else -> value.toString()
        }
    }

    fun bind(post: Post) {
        binding.apply {

            author.text = post.author
            content.text = post.content
            published.text = post.publisher
            like.isChecked = post.likedByMe
            like.text = post.likes.toString()
            share.text = formatValue(post.shares)
            view.text = post.views.toString()
            videoGroup.visibility = if (!post.video.isNullOrBlank()) View.VISIBLE else View.GONE

            youtube.setOnClickListener {
                onInteractionListener.onVideo(post)
            }

            play.setOnClickListener {
                onInteractionListener.onVideo(post)
            }

            like.setOnClickListener {
                onInteractionListener.onLike(post)
            }
            share.setOnClickListener {
                onInteractionListener.onShare(post)
            }
            menu.setOnClickListener {
                PopupMenu(it.context, it).apply {
                    inflate(R.menu.menu_post)
                    setOnMenuItemClickListener { item ->
                        when (item.itemId) {
                            R.id.remove -> {
                                onInteractionListener.onRemove(post)
                                true
                            }
                            R.id.edit -> {
                                onInteractionListener.onEdit(post)
                                true
                            }
                            else -> false
                        }
                    }
                }.show()
            }
        }
    }
}

class PostDiffCallback : DiffUtil.ItemCallback<Post>() {
    override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem == newItem
    }
}
