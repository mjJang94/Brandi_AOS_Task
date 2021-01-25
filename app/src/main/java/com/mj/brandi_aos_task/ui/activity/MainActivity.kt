package com.mj.brandi_aos_task.ui.activity

import android.content.DialogInterface
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mj.brandi_aos_task.R
import com.mj.brandi_aos_task.common.Constant
import com.mj.brandi_aos_task.databinding.ActivityMainBinding
import com.mj.brandi_aos_task.impl.DialogClickListener
import com.mj.brandi_aos_task.reponse.ImageSearchResponse
import com.mj.brandi_aos_task.ui.adapter.ListViewAdapter
import com.mj.brandi_aos_task.util.Util
import com.mj.brandi_aos_task.viewmodel.MainViewModel
import org.json.JSONObject


class MainActivity : AppCompatActivity() {


    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initData()
        initLayout()

    }

    //데이터 세팅
    private fun initData() {
        //MainviewModel 생성
        viewModel = ViewModelProvider(
            this,
            MainViewModel.MainViewModelFactory()
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
        val adapter = ListViewAdapter(this, ImageSearchResponse(), Glide.with(this))
        binding.rcvList.adapter = adapter
        binding.rcvList.layoutManager = GridLayoutManager(this, 3)

        //view model 의 query 값이 변경되면 검색 결과를 가져온다.
        viewModel.query.observe(this, Observer<String> { query ->

            //빈 데이터 삽
            viewModel.searchImageData.value = ImageSearchResponse()
            //페이지수 초기회
            viewModel.page.value = Constant.DEFAULT_SORT

            query.let {
                viewModel.showNoDataView.value = false

                //api 중복 호출 방지 조건
                if (viewModel.isLoading.value == false) {
                    if (!it.isNullOrEmpty()) {
                        viewModel.getSearchResult(true)
                    }
                }
            }
        })

        //데이터 변경이 일어나면 리스트에 데이터 전달
        viewModel.searchImageData.observe(this, Observer<ImageSearchResponse> { data ->

            data.let {
                adapter.initData()
                adapter.data = it
                adapter.notifyDataSetChanged()
            }
        })

        //recyclerview 페이징 처리
        binding.rcvList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {


                val lastPosition =
                    (recyclerView.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition() + 1
                val itemTotalCount = recyclerView.adapter?.itemCount!!


                if (lastPosition > 0 && itemTotalCount > 0) {
                    if (lastPosition == itemTotalCount && viewModel.isLoading.value == false) {
                        //결과 페이지 번호, 1~50 사이의 값, 기본 값 1
                        viewModel.page.value =
                            if (viewModel.page.value!!.toInt() < 51) (viewModel.page.value!!.toInt() + 1).toString() else viewModel.page.value

                        //50까지는 조회, 그 이상이면 토스트 출력
                        if (viewModel.page.value!!.toInt() < 51) {
                            viewModel.getSearchResult(false)
                        } else {
                            Toast.makeText(
                                this@MainActivity,
                                getString(R.string.common_last_page),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }

                super.onScrolled(recyclerView, dx, dy)
            }
        })

        //api 응답이 정상이 아닐때
        viewModel.responseError = { response ->

            try {
                val jObjError = JSONObject(response.errorBody()!!.string())
                Toast.makeText(
                    this,
                    jObjError.getString(Constant.ERRORTYPE) + jObjError.getString(Constant.ERRORMESSAGE),
                    Toast.LENGTH_SHORT
                ).show()
            } catch (e: Exception) {
                Toast.makeText(this@MainActivity, e.message.toString(), Toast.LENGTH_SHORT).show()
            }
        }

        //네트워크 에러일때
        viewModel.networkConnectionError = {
            showErrorDialog(
                getString(R.string.error_network_connection_title),
                getString(R.string.error_network_connection_msg),
                getString(R.string.common_confirm),
                Constant.EMPTY,
                object : DialogClickListener {
                    override fun positiveClick(dialog: DialogInterface) {
                        dialog.dismiss()
                    }

                    override fun negetiveClick(dialog: DialogInterface) {
                    }
                })
        }
    }

    //에러 다이얼로그
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