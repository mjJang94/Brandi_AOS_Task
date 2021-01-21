package com.mj.brandi_aos_task.ui.activity

import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mj.brandi_aos_task.R
import com.mj.brandi_aos_task.api.RetrofitConnection
import com.mj.brandi_aos_task.common.Constant
import com.mj.brandi_aos_task.databinding.ActivityMainBinding
import com.mj.brandi_aos_task.impl.DialogClickListener
import com.mj.brandi_aos_task.reponse.ImageSearchResponse
import com.mj.brandi_aos_task.ui.adapter.ListViewAdapter
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
        initLayout()

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

    private fun initLayout() {

        //리스트 어댑터 연결
        val adapter = ListViewAdapter(this, ImageSearchResponse())
        binding.rcvList.adapter = adapter
        binding.rcvList.layoutManager = GridLayoutManager(this, 3)


        //view model 의 query 값이 변경되면 검색 결과를 가져온다.
        viewModel.query.observe(this, Observer<String> { query ->
            query.let {

                GlobalScope.launch(Dispatchers.IO) {
                    delay(1000)
                    viewModel.getSearchResult()
                }
            }
        })

        //recyclerview 페이징 처리
        binding.rcvList.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val lastPosition = (recyclerView.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition() + 1
                val itemTotalCount = recyclerView.adapter?.itemCount


                if (lastPosition == itemTotalCount){
                    viewModel.page.postValue((viewModel.page.value!!.toInt() + 1).toString())
                    viewModel.getSearchResult()
                }
            }
        })


        //데이터 변경이 일어나면 리스트에 데이터 전달
        viewModel.searchImageData.observe(this, Observer<ImageSearchResponse> { data ->

            data.let {

                adapter.data = it
                adapter.notifyDataSetChanged()
            }
        })

        //Edittext 값이 변경되는 경우
        viewModel.watcher = object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //입력 값이 변경되면 비어있는 데이터를 넘겨서 리스트를 비운다
                viewModel.searchImageData.postValue(ImageSearchResponse())
            }
        }

        //정확도순, 최신순 선택값
        viewModel.onclick = View.OnClickListener { view ->
            when (view?.id) {

                binding.txtAccuracy.id -> {
                    binding.txtAccuracy.setTextColor(resources.getColor(R.color.purple_500, null))
                    binding.txtRecency.setTextColor(resources.getColor(R.color.gray, null))

                    viewModel.sort.postValue(Constant.ACCURACY)
                }

                binding.txtRecency.id -> {
                    binding.txtAccuracy.setTextColor(resources.getColor(R.color.gray, null))
                    binding.txtRecency.setTextColor(resources.getColor(R.color.purple_500, null))

                    viewModel.sort.postValue(Constant.RECENCY)
                }
            }
        }

        //정확도순이 기본값
        binding.txtAccuracy.isSelected = true
        viewModel.searchTypeAccurancyClick()
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