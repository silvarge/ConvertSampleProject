package com.example.convertsampleproj.ui.convert

import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.NumberPicker
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.convertsampleproj.R
import com.example.convertsampleproj.databinding.FragmentConvertBinding
import com.example.convertsampleproj.domain.model.LengthEnum
import com.example.convertsampleproj.domain.model.PickerEnum
import com.example.convertsampleproj.domain.usecase.ConvertUseCase
import com.example.convertsampleproj.ui.modal.ConvertCustomDialog

/**
 * A simple [Fragment] subclass.
 * Use the [ConvertFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ConvertFragment : Fragment(), ConvertCustomDialog.OnConvertModalListener {
  //  binding 해주기 (뷰 바인딩 설정)
  private lateinit var binding: FragmentConvertBinding
  
  private lateinit var convertUseCase: ConvertUseCase
  private lateinit var convertViewModel: ConvertViewModel
  
  override fun onAttach(context: Context) {
    super.onAttach(context)
//    audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
//    mediaPlayer = MediaPlayer.create(context, R.raw.test_audio)
  }
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
  }
  
  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
  ): View? {
    // Inflate the layout for this fragment
    binding = FragmentConvertBinding.inflate(inflater, container, false)
    convertUseCase = ConvertUseCase()
    convertViewModel = ViewModelProvider(this)[ConvertViewModel::class.java]

//    Picker 값 실시간 업데이트
    setValuePicker(convertViewModel, binding.npConvSelectType)
//    convertViewModel.updatePickerValues(PickerValue.entries.toTypedArray())
//    단위 변환
    setUpListeners()
    
    return binding.root
  }
  
  // 이벤트 따라 단위 변환 - ValuePicker 변환 시는 setValuePicker 함수에서 변환 실행
  private fun setUpListeners() {
    binding.tvResult.setOnClickListener {
      showConvertModal("Result")
    }
    
    binding.tvValue.setOnClickListener {
      showConvertModal("Value")
    }
    
    binding.etBeforeValue.addTextChangedListener(object : TextWatcher {
      override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
      override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
      override fun afterTextChanged(p0: Editable?) {
        performConversionUnit()
      }
    })
  }
  
  //  [Function] 단위 변환
  private fun performConversionUnit() {
    val value = binding.etBeforeValue.text.toString().toDoubleOrNull()
    val cardIndex = PickerEnum.getIndexByDisplayName(binding.tvConvCard.text.toString())
    val befIndex = LengthEnum.getIndexByDisplayName(binding.tvMainUnit.text.toString())
    val aftIndex = LengthEnum.getIndexByDisplayName(binding.tvRstMainUnit.text.toString())
    
    if (value != null) {  // Value 값이 null이 아닌 숫자(Double)값 일 때 로직 수행
      val result = when (cardIndex) {
        0 -> convertUseCase.performHydrogenConversion(value, befIndex, aftIndex)
        1 -> cardIndex
        2 -> cardIndex
        3 -> cardIndex
        else -> "Not Yet"
      }
      binding.etRstValue.setText(result.toString())
    } else {  // Null 일 때 혹은 예외 사항 생겼을 때
      binding.etRstValue.setText("Not A Number!")
    }
  }
  
  //  [Function] Picker 업데이트에 따른 Card 값 업데이트
  private fun setValuePicker(
    viewModel: ConvertViewModel, npValuePicker: NumberPicker
  ) {
    viewModel.pickerValues.observe(viewLifecycleOwner) { values ->
      npValuePicker.minValue = 0
      npValuePicker.maxValue = values.size - 1
      npValuePicker.displayedValues = values
      npValuePicker.wrapSelectorWheel = true
    }
//    선택된 인덱스 관찰
    viewModel.selectedIndex.observe(viewLifecycleOwner) { index ->
      npValuePicker.value = index
    }

//    변경 이벤트 발생 (picker 돌렸을 때)
    npValuePicker.setOnValueChangedListener { _, _, newValue ->
      viewModel.updateSelectedIndex(newValue)
    }

//    Card의 Value 값 변환
    viewModel.text.observe(viewLifecycleOwner) {
      binding.tvConvCard.text = it
      performConversionUnit()
    }
  }
  
  // [Function] Modal
  private fun showConvertModal (type: String){
    val modalFragment = ConvertCustomDialog()
    modalFragment.setOnConvertModalListener(this)
    modalFragment.show(childFragmentManager, "ConvertModalFragment")
  }
  
  override fun OnSelectConvertUnit(selectedUnit: String, enteredValue: Double) {
    performConversionUnit()
  }
  
}