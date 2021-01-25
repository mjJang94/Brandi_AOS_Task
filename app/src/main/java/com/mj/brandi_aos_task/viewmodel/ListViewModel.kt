package com.mj.brandi_aos_task.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ListViewModel : ViewModel() {


    class ListViewModelFactory: ViewModelProvider.Factory {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return ListViewModel() as T
        }
    }
}