package com.mj.brandi_aos_task.ui.activity

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import com.mj.brandi_aos_task.R
import com.mj.brandi_aos_task.api.RetrofitConnection
import com.mj.brandi_aos_task.databinding.ActivityMainBinding
import com.mj.brandi_aos_task.impl.DialogClickListener
import com.mj.brandi_aos_task.reponse.ImageSearchResponse
import com.mj.brandi_aos_task.util.Util
import com.mj.brandi_aos_task.viewmodel.MainViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject


class MainActivity : AppCompatActivity() {

    private val apiConnection: RetrofitConnection by inject()
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initData()


    }

    //데이터 세팅 초기화
    private fun initData() {
        //MainviewModel 생성
        viewModel = ViewModelProvider(
            this,
            MainViewModel.MainViewModelFactory(apiConnection)
        ).get(MainViewModel::class.java)
        //binding
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        //ViewMoel 등록
        binding.mainModel = viewModel
        //Lifecycle 등록
        binding.lifecycleOwner = this

    }

    private fun initLayout(){

        //view model 의 query 값이 변경되면 검색 결과를 가져온다.
        viewModel.query.observe(this, Observer<String>{ query ->
            query.let {

                GlobalScope.launch(Dispatchers.IO){
                    delay(1000)
                    viewModel.getSearchResult()
                }
            }
        })

        viewModel.searchImageData.observe(this, Observer<ImageSearchResponse>{ data ->

            data.let {

            }
        })
    }


    private fun showErrorDialog(
        title: String,
        msg: String,
        positiveBtnText: String,
        negetiveBtnText: String,
        listener: DialogClickListener
    ) {
        Util().showErrorDialog(this, title, msg, positiveBtnText, negetiveBtnText, listener)
    }
}