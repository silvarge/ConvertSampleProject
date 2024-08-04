package com.example.convertsampleproj.domain.model

enum class LengthEnum (val displayName: String) {
  CENTIMETER("cm"),
  METER("m"),
  MILLIMETER("mm"),
  KILOMETER("km"),
  INCH("in"),
  FEET("ft");
  
  companion object {
    fun getIndexByDisplayName(displayName: String): Int {
      return entries.toTypedArray().indexOfFirst { it.displayName == displayName }
    }
  }
}