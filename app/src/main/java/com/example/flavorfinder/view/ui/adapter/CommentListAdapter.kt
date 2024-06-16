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

class CommentListAdapter(
    private val userProfileMap: Map<String, GetUserProfileResponse>
): ListAdapter<CommentData, CommentListAdapter.ViewHolder>(DIFF_CALLBACK) {

    class ViewHolder(val binding: ItemCardCommentBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(comment: CommentData, userProfile: GetUserProfileResponse?) {
            binding.apply {
                userProfile?.let {
                    tvUsername.text = it.data.username
                    Glide.with(itemView.context)
                        .load(it.data.imgUrl)
                        .placeholder(R.drawable.baseline_account_circle_24)
                        .into(ivProfile)
                }
                tvDate.text = comment.createdAt
                tvContent.text = comment.commentText
            }
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
        val commentItem = getItem(position)
        val userProfile = userProfileMap[commentItem.userId]
        holder.bind(commentItem, userProfile)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<CommentData>() {
            override fun areItemsTheSame(oldItem: CommentData, newItem: CommentData): Boolean {
                return oldItem.commentId == newItem.commentId
            }

            override fun areContentsTheSame(oldItem: CommentData, newItem: CommentData): Boolean {
                return oldItem == newItem
            }

        }
    }
}