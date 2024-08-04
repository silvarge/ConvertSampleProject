package com.example.convertsampleproj.domain.usecase

import android.util.Log

// 수소 변환 시 관련 로직 함수
class HydrogenConvert {
  fun convertUnit (val1: Double, befType: Int, aftType:Int): Number {
    val cmValue = if (befType == 0) val1 else convertStanCm(val1, befType)
    
    val resultValue: Double = when (aftType) {
      0 -> cmValue
      1 -> cmValue / 100
      2 -> cmValue * 10
      3 -> cmValue / 100000
      4 -> cmValue / 2.54
      5 -> cmValue / 30.48
      else -> -1.0
    }
    
    Log.d("ConvertTracker", "${val1}, ${befType}, ${aftType}, $resultValue ")
    
    return resultValue
  }
  
  fun convertStanCm (inp: Double, type: Int): Double {
    val stanCm: Double = when (type) {
      1 -> inp * 100
      2 -> inp / 10
      3 -> inp * 100000
      4 -> inp * 2.54
      5 -> inp * 30.48
      else -> inp
    }
    return stanCm
  }
}
