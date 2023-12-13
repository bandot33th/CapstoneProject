package com.example.capstoneproject.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.capstoneproject.data.response.main.history.ImagesItem
import com.example.capstoneproject.databinding.TrashLayoutBinding
import com.example.capstoneproject.util.convertGMTTimestamp

class HistoryAdapter: RecyclerView.Adapter<HistoryAdapter.ListViewHolder>() {

    private val listImagesItem = ArrayList<ImagesItem>()
    fun setData(data: ArrayList<ImagesItem>){
        listImagesItem.clear()
        listImagesItem.addAll(data)
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HistoryAdapter.ListViewHolder {
        val binding = TrashLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryAdapter.ListViewHolder, position: Int) {
        val user = listImagesItem[position]
        holder.bind(user)
    }

    override fun getItemCount(): Int {
        return listImagesItem.size
    }

    class ListViewHolder(private val binding: TrashLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(trash: ImagesItem) {
            binding.trashDescriptionTextView.text = trash.type
            val (formattedTime, formattedDate) = convertGMTTimestamp(trash.timestamp)
            binding.dateTextView.text = formattedDate
            binding.timeTextView.text = formattedTime
            Glide.with(itemView.context)
                .load(trash.imageUrl)
                .into(binding.trashImageView)
        }
    }
    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ImagesItem>() {
            override fun areItemsTheSame(oldItem: ImagesItem, newItem: ImagesItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ImagesItem, newItem: ImagesItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}