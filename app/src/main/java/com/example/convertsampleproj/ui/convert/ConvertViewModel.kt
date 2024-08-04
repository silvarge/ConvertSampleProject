package com.example.convertsampleproj.ui.convert

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.convertsampleproj.domain.model.PickerEnum

class ConvertViewModel : ViewModel() {
  private val _pickerValues = MutableLiveData<Array<String>>().apply {
    value = PickerEnum.entries.map { it.displayName }.toTypedArray()
  }
  
  val pickerValues: LiveData<Array<String>> = _pickerValues
  
  private val _selectedIndex = MutableLiveData<Int>().apply {
    value = 0 // Default Index
  }
  
  val selectedIndex: LiveData<Int> = _selectedIndex
  
  private val _text = MediatorLiveData<String>().apply {
    addSource(_selectedIndex) { index ->
      value = _pickerValues.value?.get(index) ?: "선택된 값이 없습니다."
    }
  }
  
  val text: LiveData<String> = _text
  
  //  인덱스 업데이트
  fun updateSelectedIndex(index: Int) {
    _selectedIndex.value = index
  }
}