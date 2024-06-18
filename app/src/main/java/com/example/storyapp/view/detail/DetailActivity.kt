package com.example.storyapp.view.detail

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.storyapp.R
import com.example.storyapp.data.remote.response.DetailStoryResponse
import com.example.storyapp.databinding.ActivityDetailBinding
import com.example.storyapp.helper.DateHelper.formatDate
import com.example.storyapp.view.ViewModelFactory
import kotlinx.coroutines.launch

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private val detailViewModel by viewModels<DetailViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val storyId = intent.getStringExtra("story_id")

        lifecycleScope.launch {
            showLoading(true)
            try {
                val detailStory = storyId?.let { detailViewModel.getDetailStory(it) }
                if (detailStory != null) {
                    showDetailStory(detailStory)
                }
            } catch (e: Exception) {
                Toast.makeText(this@DetailActivity,
                    getString(R.string.failed_load_detail_story), Toast.LENGTH_SHORT).show()
                e.printStackTrace()
            }
            showLoading(false)
        }
    }

    private fun showDetailStory(detailStory: DetailStoryResponse) {
        if (detailStory.story != null) {
            val story = detailStory.story
            binding.apply {
                tvDetailUsername.text = story.name ?: ""
                tvDetailDescription.text = story.description ?: ""
                val formatDate = story.createdAt?.let { formatDate(it) } ?: ""
                tvDetailDate.text = formatDate
                Glide.with(this@DetailActivity)
                    .load(story.photoUrl)
                    .into(imgDetailPhoto)
            }
        } else {
            Toast.makeText(this, getString(R.string.detail_story_notfound), Toast.LENGTH_SHORT).show()
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}