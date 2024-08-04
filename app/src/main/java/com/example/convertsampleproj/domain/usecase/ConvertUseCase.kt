package com.example.convertsampleproj.domain.usecase

class ConvertUseCase(private val hydrogenConvert: HydrogenConvert) {
  
  fun performHydrogenConversion(val1: Double, val2: Double, type1: Int, type2: Int): Number {
    return hydrogenConvert.convertUnit(val1, val2, type1, type2)
  }
}