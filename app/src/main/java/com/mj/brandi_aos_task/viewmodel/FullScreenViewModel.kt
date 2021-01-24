package com.mj.brandi_aos_task.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.request.RequestListener

class FullScreenViewModel: ViewModel() {

    //이미지 url
    var imageURL: MutableLiveData<String> = MutableLiveData()
    var displaySiteName: MutableLiveData<String> = MutableLiveData()
    var dateTime: MutableLiveData<String> = MutableLiveData()



    class FullScreenVieModelFactory : ViewModelProvider.Factory {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return FullScreenViewModel() as T
        }
    }
}