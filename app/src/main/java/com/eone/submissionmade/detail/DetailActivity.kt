package com.eone.submissionmade.detail

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.eone.core.BuildConfig
import com.eone.core.domain.model.Content
import com.eone.submissionmade.R
import com.eone.submissionmade.databinding.ActivityDetailBinding
import com.shashank.sony.fancytoastlib.FancyToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private val viewModel: DetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val detailContent = intent.getParcelableExtra<Content>(EXTRA_DETAIL)
        if (detailContent != null) {
            setupView(detailContent)
        }
    }


    private fun setupView(content: Content) {
        binding.apply {
            backButton.setOnClickListener {
                onBackPressed()
            }

            Glide.with(this@DetailActivity)
                .load(BuildConfig.IMAGE_URL + content.posterPath)
                .into(ivBackground)
            ivBackground.tag = content.posterPath

            Glide.with(this@DetailActivity)
                .load(BuildConfig.IMAGE_URL + content.posterPath)
                .into(subPoster)
            subPoster.tag = content.posterPath

            tvTitle.text = content.title
            tvDate.text = content.releaseDate
            tvVote.text = content.voteAverage.toString()
            tvOverview.text = content.overview
            tvPopularity.text = content.popularity.toString()

            var favoriteState = content.favorite
            setFavoriteState(favoriteState)
            binding.favoriteButton.setOnClickListener {
                favoriteState = !favoriteState
                viewModel.setFavoriteMovie(content, favoriteState)
                setFavoriteState(favoriteState)
                if (favoriteState) {
                    showFavoriteToast(true)
                } else {
                    showFavoriteToast(false)
                }
            }
        }
    }

    private fun showFavoriteToast(add: Boolean) {
        if (add) {
            FancyToast.makeText(
                applicationContext,
                getString(R.string.addToFavorite),
                FancyToast.LENGTH_SHORT,
                FancyToast.SUCCESS,
                R.drawable.ic_happy,
                false
            ).show()
        } else {
            FancyToast.makeText(
                applicationContext,
                getString(R.string.removeFromFavorite),
                FancyToast.LENGTH_SHORT,
                FancyToast.ERROR,
                R.drawable.ic_sad,
                false
            ).show()
        }
    }

    private fun setFavoriteState(state: Boolean) {
        binding.apply {
            if (state) {
                favoriteButton.setImageDrawable(
                    ContextCompat.getDrawable(
                        this@DetailActivity,
                        R.drawable.ic_favorite
                    )
                )
            } else {
                favoriteButton.setImageDrawable(
                    ContextCompat.getDrawable(
                        this@DetailActivity,
                        R.drawable.ic_favorite_border
                    )
                )
            }
        }
    }


    companion object {
        const val EXTRA_DETAIL = "EXTRA DETAIL"
    }
}