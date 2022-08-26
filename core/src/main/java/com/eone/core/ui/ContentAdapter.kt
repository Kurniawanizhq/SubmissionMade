package com.eone.core.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.eone.core.BuildConfig
import com.eone.core.R
import com.eone.core.databinding.ListContentBinding
import com.eone.core.domain.model.Content
import com.eone.core.utils.Callback
import com.eone.core.utils.ContentDiffCallback

class ContentAdapter (private val callback: Callback) : RecyclerView.Adapter<ContentAdapter.MovieViewHolder>() {

    private var listContents = ArrayList<Content>()

    fun setContent(contents: List<Content>?) {
        if (contents != null) {
            val diffCallback = ContentDiffCallback(listContents, contents)
            val diffResult = DiffUtil.calculateDiff(diffCallback)
            listContents.clear()
            listContents.addAll(contents)
            diffResult.dispatchUpdatesTo(this)
        }
    }

    fun getSwipedData(swipedPosition: Int): Content {
        return listContents[swipedPosition]
    }


    inner class MovieViewHolder(private val binding: ListContentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(content : Content) {
            binding.apply {
                tvUserScore.text = content .voteAverage.toString()
                tvTitleHome.text = content .title
                tvDate.text = content .releaseDate

                Glide.with(itemView.context)
                    .load(BuildConfig.IMAGE_URL + content .posterPath)
                    .placeholder(R.drawable.picture_placeholder_small)
                    .error(BuildConfig.IMAGE_URL)
                    .into(ivItem)

                itemCard.setOnClickListener {
                    callback.onItemClicked(content )
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val itemsMoviesBinding =
            ListContentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(itemsMoviesBinding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = listContents[position]
        holder.bind(movie)
    }

    override fun getItemCount(): Int = listContents.size

}