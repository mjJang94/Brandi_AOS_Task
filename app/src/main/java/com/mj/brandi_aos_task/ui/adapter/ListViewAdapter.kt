package com.mj.brandi_aos_task.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.mj.brandi_aos_task.R
import com.mj.brandi_aos_task.databinding.RowSearchListBinding
import com.mj.brandi_aos_task.reponse.ImageSearchResponse
import com.mj.brandi_aos_task.reponse.ImageSearchResponse.Info

class ListViewAdapter(val context: Context, var data: ImageSearchResponse) :
    RecyclerView.Adapter<ListViewAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = RowSearchListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(data.documents?.get(position)!!, context)
    }

    override fun getItemCount(): Int {
        return data.documents?.size ?: 0
    }


    inner class Holder(private var binding: RowSearchListBinding) :
        RecyclerView.ViewHolder(binding.root) {


        fun bind(data: Info, context: Context) {

            Glide.with(context)
                .load(data.thumbnail_url)
                .override(150,150)
                .fitCenter()
                .thumbnail(0.1f)
                .skipMemoryCache(false)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(binding.ivRow)
        }
    }
}