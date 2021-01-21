package com.mj.brandi_aos_task.viewmodel

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import androidx.databinding.BindingAdapter
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
    var query: MutableLiveData<String> = MutableLiveData()

    //페이지 번호
    var page: MutableLiveData<String> = MutableLiveData(Constant.DEFAULT_SORT)

    //문서 정렬방식
    var sort: MutableLiveData<String> = MutableLiveData(Constant.ACCURACY)

    //검색 데이터
    var searchImageData: MutableLiveData<ImageSearchResponse> = MutableLiveData()

    //검색결과 존재 여부 구분값
    var resultDataExist: MutableLiveData<Boolean> = MutableLiveData(true)

    var watcher: TextWatcher? = null

    var onclick: View.OnClickListener? = null


//    //통신 에러 처리
//    var responseError: (() -> Unit)? = null
//
//    //네트워크 에러 처리
//    var networkConnectionError: (() -> Unit)? = null


    //Edittext 입력시 데이터 비워줌
//    val watcher = object : TextWatcher {
//        override fun afterTextChanged(p0: Editable?) {
//        }
//
//        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//        }
//
//        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//            searchImageData.postValue(ImageSearchResponse())
//        }
//    }


//    val onClick = object : View.OnClickListener{
//        override fun onClick(view: View?) {
//
//            when(view){
//                R.id.
//            }
//        }
//    }

    //정확도순 선택시
    fun searchTypeAccurancyClick() {
        sort.postValue(Constant.ACCURACY)
    }

    //최신순 선택시
    fun searchTypeRecencyClick() {
        sort.postValue(Constant.RECENCY)
    }


    //입력 삭제 버튼 클릭 메소드
    fun removeBtnClick() {
        query.postValue(Constant.EMPTY)
    }

    //이미지 검색 메소드
    fun getSearchResult() {

        retrofitConnection.getCovidInfo(
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
                        resultDataExist.postValue(false)
                    } else {
                        //검색결과 존재
                        resultDataExist.postValue(true)

                        //기존데이터 있으면 이어서 저장
                        if (searchImageData.value != null) {
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
//                        responseError?.let {
//                            it()
//                        }
                }
            }

            override fun onFailure(call: Call<ImageSearchResponse>, t: Throwable) {
//                    networkConnectionError?.let {
//                        it()
//                    }
            }
        })
    }


    class MainViewModelFactory(private val retrofitConnection: RetrofitConnection) :
        ViewModelProvider.Factory {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return MainViewModel(retrofitConnection) as T
        }
    }
}