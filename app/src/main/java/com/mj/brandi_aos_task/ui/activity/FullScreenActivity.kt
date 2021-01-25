package com.mj.brandi_aos_task.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.mj.brandi_aos_task.R
import com.mj.brandi_aos_task.common.Constant
import com.mj.brandi_aos_task.databinding.ActivityFullScreenBinding
import com.mj.brandi_aos_task.viewmodel.FullScreenViewModel

class FullScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFullScreenBinding
    private lateinit var viewModel: FullScreenViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_full_screen)

        initData()
        initLayout()
    }

    //데이터 세팅
    private fun initData() {

        viewModel = ViewModelProvider(this, FullScreenViewModel.FullScreenVieModelFactory())
            .get(FullScreenViewModel::class.java)
        //binding
        binding = DataBindingUtil.setContentView(this, R.layout.activity_full_screen)
        //ViewMoel 등록
        binding.fullScreenModel = viewModel
        //Lifecycle 등록
        binding.lifecycleOwner = this

        getData()
    }

    //이전 화면에서 넘겨받는 데이터
    private fun getData() {
        viewModel.imageURL.value = intent.getStringExtra(Constant.IMAGE_URL) ?: Constant.EMPTY
        viewModel.displaySiteName.value = intent.getStringExtra(Constant.DISPLAY_SITENAME) ?: Constant.EMPTY
        viewModel.dateTime.value = intent.getStringExtra(Constant.DATETIME) ?: Constant.EMPTY
    }

    private fun initLayout(){
        Glide.with(this)
            .load(viewModel.imageURL.value)
            .thumbnail(0.1f)
            .override(500, 500)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .placeholder(R.drawable.loading_image)
            .error(R.drawable.can_not_load)
            .into(binding.ivFullScreen)


    }
}