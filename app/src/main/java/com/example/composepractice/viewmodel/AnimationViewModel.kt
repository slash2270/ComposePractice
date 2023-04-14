package com.example.composepractice.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AnimationViewModel : ViewModel() {
    //首页选中项的索引
    private val _position = MutableLiveData(-1)
    //动画状态
    var animalBoolean = mutableStateOf(true)
    var position: LiveData<Int> = _position
    //选中索引数据刷新
    var bootomType=true
    fun positionChanged(selectedIndex: Int){
        _position.value=selectedIndex
    }
}