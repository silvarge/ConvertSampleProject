package com.example.convertsampleproj.domain.model

import android.util.Log

enum class PickerEnum (val displayName: String) {
  HYDROGEN("H₂"),
  OXYGEN("O₂"),
  NITROGEN("N₂"),
  ARGON("Ar");
  
  companion object {
    fun getIndexByDisplayName(displayName: String): Int {
      return PickerEnum.entries.toTypedArray().indexOfFirst { it.displayName == displayName }
    }
  }
}