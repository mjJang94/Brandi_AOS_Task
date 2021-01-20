package com.mj.brandi_aos_task.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.mj.brandi_aos_task.api.RetrofitConnection
import com.mj.brandi_aos_task.common.Constant
import com.mj.brandi_aos_task.reponse.ImageSearchResponse
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(val retrofitConnection: RetrofitConnection) : ViewModel() {

    private val coroutineExceptionHanlder =
        CoroutineExceptionHandler { coroutineContext, throwable ->
            throwable.printStackTrace()
        }

    private val ioDispatchers = Dispatchers.IO + coroutineExceptionHanlder
    private val uiDispatchers = Dispatchers.Main + coroutineExceptionHanlder

    //검색어
    var query = MutableLiveData<String>().apply {
        value = Constant.EMPTY
    }

    //페이지 번호
    var page = MutableLiveData<String>().apply {
        value = Constant.EMPTY
    }

    //응답 데이터
    var searchImageData = MutableLiveData<ImageSearchResponse>().apply {
        value = ImageSearchResponse()
    }

    //통신 에러 처리
    var responseError: (() -> Unit)? = null

    //네트워크 에러 처리
    var networkConnectionError: (() -> Unit)? = null


    //이미지 검색 메소드
    fun getSearchResult() {

        viewModelScope.launch(ioDispatchers) {
            retrofitConnection.getCovidInfo(
                query.value!!,
                Constant.ACCURACY,
                page.value!!,
                Constant.STATIC_SIZE
            ).enqueue(object : Callback<ImageSearchResponse> {

                override fun onResponse(
                    call: Call<ImageSearchResponse>,
                    response: Response<ImageSearchResponse>
                ) {
                    if (response.isSuccessful) {
                        val tmpData = response.body()
                        searchImageData.postValue(tmpData)

                    } else {
                        responseError?.let {
                            it()
                        }
                    }
                }

                override fun onFailure(call: Call<ImageSearchResponse>, t: Throwable) {
                    networkConnectionError?.let {
                        it()
                    }
                }
            })
        }
    }


    class MainViewModelFactory(private val retrofitConnection: RetrofitConnection) :
        ViewModelProvider.Factory {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return MainViewModel(retrofitConnection) as T
        }
    }
}