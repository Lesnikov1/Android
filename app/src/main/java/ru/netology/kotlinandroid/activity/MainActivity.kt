package ru.netology.kotlinandroid.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import ru.netology.kotlinandroid.R
import ru.netology.kotlinandroid.adapter.OnInteractionListener
import ru.netology.kotlinandroid.adapter.PostAdapter
import ru.netology.kotlinandroid.databinding.ActivityMainBinding
import ru.netology.kotlinandroid.dto.Post
import ru.netology.kotlinandroid.util.AndroidUtils
import ru.netology.kotlinandroid.viewmodel.PostViewModel
import ru.netology.nmedia.activity.NewPostResultContract

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel: PostViewModel by viewModels()
        val adapter = PostAdapter(object : OnInteractionListener {

            override fun onVideo(post: Post) {
                val videoUrl = post.video
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(videoUrl))
                startActivity(intent)
            }

            override fun onLike(post: Post) {
                viewModel.like(post.id)
            }

            override fun onShare(post: Post) {
                val intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, post.content)
                    type = "text/plain"
                }
                val shareIntent =
                    Intent.createChooser(intent, getString(R.string.chooser_share_post))
                startActivity(shareIntent)
            }

            override fun onRemove(post: Post) {
                viewModel.removeById(post.id)
            }

            override fun onEdit(post: Post) {
                viewModel.edit(post)
                newPostLauncher.launch(post.content)
            }

            override fun onClearEdit() {
                viewModel.clearEdit()
            }

            val newPostLauncher = registerForActivityResult(NewPostResultContract()) { result ->
                result ?: return@registerForActivityResult
                viewModel.changeContent(result)
                viewModel.save()
            }
        }
        )

        binding.list.adapter = adapter
        viewModel.data.observe(this) { posts ->
            val nwePost = posts.size > adapter.currentList.size && adapter.currentList.isNotEmpty()

            adapter.submitList(posts) {
                if (nwePost) {
                    binding.list.smoothScrollToPosition(0)
                }
            }
        }

        val newPostLauncher = registerForActivityResult(NewPostResultContract()) { result ->
            result ?: return@registerForActivityResult
            viewModel.changeContent(result)
            viewModel.save()
        }

        binding.fab.setOnClickListener {
            newPostLauncher.launch(null)
        }
    }
}
