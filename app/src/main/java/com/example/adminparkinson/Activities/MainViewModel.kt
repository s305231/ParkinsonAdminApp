package com.example.adminparkinson.Activities

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore

class MainViewModel(store: ViewModelStore, factory: Factory) : ViewModelProvider(store, factory) {

     var itemMutableList: MutableLiveData<String> = MutableLiveData()

    open fun setText(s: String) {

        itemMutableList.value = s


    }

    open fun getText() : MutableLiveData<String> {
        return itemMutableList
    }


}