package com.example.convertsampleproj.domain.usecase

class ConvertUseCase() {
  fun performHydrogenConversion(val1: Double, befType: Int, aftType: Int): Number {
    val hydrogenConvert = HydrogenConvert()
    return hydrogenConvert.convertUnit(val1, befType, aftType)
  }
}