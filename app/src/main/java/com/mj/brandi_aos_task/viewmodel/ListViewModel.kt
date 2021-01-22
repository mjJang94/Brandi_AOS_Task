package com.mj.brandi_aos_task.viewmodel

import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mj.brandi_aos_task.api.RetrofitConnection

class ListViewModel : ViewModel() {

    var rowClick: (() -> Unit)? = null


    class ListViewModelFactory: ViewModelProvider.Factory {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return ListViewModel() as T
        }
    }
}