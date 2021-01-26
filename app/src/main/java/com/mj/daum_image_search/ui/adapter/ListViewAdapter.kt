package com.mj.daum_image_search.ui.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.mj.daum_image_search.common.Constant
import com.mj.daum_image_search.databinding.RowSearchListBinding
import com.mj.daum_image_search.reponse.ImageSearchResponse
import com.mj.daum_image_search.reponse.ImageSearchResponse.Info
import com.mj.daum_image_search.ui.activity.FullScreenActivity

class ListViewAdapter(val context: Context, var data: ImageSearchResponse,var glide: RequestManager) :
    RecyclerView.Adapter<ListViewAdapter.Holder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding =
            RowSearchListBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(data.documents?.get(position)!!, context)
    }

    override fun getItemCount(): Int {
        return data.documents?.size ?: 0
    }

    fun initData() {
        data = ImageSearchResponse()
        notifyDataSetChanged()
    }


    inner class Holder(private var binding: RowSearchListBinding) : RecyclerView.ViewHolder(binding.root) {


        fun bind(data: Info, context: Context) {

            glide
                .load(data.thumbnail_url)
                .override(150, 150)
                .centerCrop()
                .thumbnail(0.1f)
                .into(binding.ivRow)


            binding.ivRow.setOnClickListener {
                val intent = Intent(context, FullScreenActivity::class.java)
                intent.putExtra(Constant.IMAGE_URL, data.image_url)
                intent.putExtra(Constant.DISPLAY_SITENAME, data.display_sitename ?: "")
                intent.putExtra(Constant.DATETIME, data.datetime ?: "")
                context.startActivity(intent)
            }

        }
    }
}