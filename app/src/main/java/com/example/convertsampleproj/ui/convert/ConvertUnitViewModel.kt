package com.example.convertsampleproj.ui.convert

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ConvertUnitViewModel: ViewModel() {
  private val _selectedUnitItem = MutableLiveData<String>().apply { value = "" }
  
  val selectedUnitItem: LiveData<String> = _selectedUnitItem
  
  fun selectItem(item:String) {
    _selectedUnitItem.value = item
    Log.d("ConvertFragment-selectItem", "selectedUnitItem observed: ${selectedUnitItem.value}")
    Log.d("ConvertFragment-selectItem", "LiveData observed: ${_selectedUnitItem.value}")
  }
}