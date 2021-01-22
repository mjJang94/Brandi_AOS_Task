package com.mj.brandi_aos_task.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.android.material.snackbar.Snackbar
import com.mj.brandi_aos_task.api.RetrofitConnection
import com.mj.brandi_aos_task.common.Constant
import com.mj.brandi_aos_task.reponse.ImageSearchResponse
import kotlinx.coroutines.*
import okhttp3.ResponseBody
import org.json.JSONObject
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.component.inject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel(), KoinComponent {

    private val apiConnection: RetrofitConnection by inject()

    private val coroutineExceptionHanlder =
        CoroutineExceptionHandler { coroutineContext, throwable ->
            throwable.printStackTrace()
        }

    private val ioDispatchers = Dispatchers.IO + coroutineExceptionHanlder
    private val mainDispatchers = Dispatchers.Main + coroutineExceptionHanlder

    //검색어
    var query: MutableLiveData<String> = MutableLiveData()

    //페이지 번호
    var page: MutableLiveData<String> = MutableLiveData(Constant.DEFAULT_SORT)

    //문서 정렬방식
    var sort: MutableLiveData<String> = MutableLiveData(Constant.ACCURACY)

    //검색 데이터
    var searchImageData: MutableLiveData<ImageSearchResponse> = MutableLiveData()

    //검색결과 존재 여부 구분값
    var showNoDataView: MutableLiveData<Boolean> = MutableLiveData(false)

    //통신 중 중복 통신 막기
    var isLoading: MutableLiveData<Boolean> = MutableLiveData(false)

    //통신 에러 처리
    var responseError: ((response: Response<ImageSearchResponse>) -> Unit)? = null

    //네트워크 에러 처리
    var networkConnectionError: (() -> Unit)? = null

    //입력 삭제 버튼 클릭 메소드
    fun removeBtnClick() {
        query.value = Constant.EMPTY
        page.value = Constant.DEFAULT_SORT
    }

    //이미지 검색 메소드
    fun getSearchResult(isNewKeyword: Boolean) {

        isLoading.postValue(true)

        viewModelScope.launch(ioDispatchers) {

            //검색어 입력시 1초 지연 검색 아닐경우 페이징
            if(isNewKeyword) {
                delay(1000)
            }

            Log.e("page: ", page.value.toString())
            Log.e("searchImageData: ", searchImageData.value?.documents?.size.toString())

            apiConnection.startImageSearch(
                query.value!!,
                sort.value!!,
                page.value!!,
                Constant.STATIC_SIZE
            ).enqueue(object : Callback<ImageSearchResponse> {

                override fun onResponse(
                    call: Call<ImageSearchResponse>,
                    response: Response<ImageSearchResponse>
                ) {

                    if (response.isSuccessful) {

                        val tmpData = response.body()

                        //검색결과 없음
                        if (tmpData?.documents.isNullOrEmpty() && tmpData?.meta?.is_end!!) {
                            showNoDataView.postValue(true)
                        } else {
                            //검색결과 존재
                            showNoDataView.postValue(false)

                            //기존데이터 있으면 이어서 저장
                            if (searchImageData.value?.documents != null) {

                                //기존 데이터 저장
                                val tmpList = searchImageData.value ?: ImageSearchResponse()

                                //새로운 데이터의 element 담기
                                for (element in tmpData?.documents!!) {
                                    tmpList.documents?.add(element)
                                }
                                //LiveData 에 통지
                                searchImageData.postValue(tmpList)

                                //기존데이터 없으면 새로운 데이터로 저장
                            } else {
                                searchImageData.postValue(tmpData)
                            }
                        }
                    } else {
                        responseError?.let { it(response)}




                    }
                    isLoading.postValue(false)
                }

                override fun onFailure(call: Call<ImageSearchResponse>, t: Throwable) {
//                    networkConnectionError?.let {
//                        it()
//                    }
                    isLoading.postValue(false)
                }
            })

        }
    }


    class MainViewModelFactory : ViewModelProvider.Factory {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return MainViewModel() as T
        }
    }
}