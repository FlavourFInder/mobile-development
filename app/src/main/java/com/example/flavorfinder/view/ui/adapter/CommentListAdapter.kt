package com.example.flavorfinder.view.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.flavorfinder.R
import com.example.flavorfinder.databinding.ItemCardCommentBinding
import com.example.flavorfinder.network.response.CommentData
import com.example.flavorfinder.network.response.GetUserProfileResponse
import com.example.flavorfinder.pref.CommentWithUserProfile

class CommentListAdapter: ListAdapter<CommentWithUserProfile, CommentListAdapter.ViewHolder>(DIFF_CALLBACK) {

    class ViewHolder(val binding: ItemCardCommentBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(comment: CommentWithUserProfile) {
            binding.tvUsername.text = comment.userProfile.username
            binding.tvContent.text = comment.comment.commentText
            Glide.with(itemView.context)
                .load(comment.userProfile.imgUrl)
                .placeholder(R.drawable.baseline_account_circle_24)
                .into(binding.ivProfile)
            binding.tvDate.text = comment.comment.createdAt.slice(0..9)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = ItemCardCommentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CommentListAdapter.ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<CommentWithUserProfile>() {
            override fun areItemsTheSame(oldItem: CommentWithUserProfile, newItem: CommentWithUserProfile): Boolean {
                return oldItem.comment.commentId == newItem.comment.commentId
            }

            override fun areContentsTheSame(oldItem: CommentWithUserProfile, newItem: CommentWithUserProfile): Boolean {
                return oldItem == newItem
            }

        }
    }
}