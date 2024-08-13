package com.example.convertsampleproj.ui.convert

import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.ToneGenerator
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.SoundEffectConstants
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.NumberPicker
import android.widget.Spinner
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.convertsampleproj.R
import com.example.convertsampleproj.databinding.FragmentConvertBinding
import com.example.convertsampleproj.domain.model.LengthEnum
import com.example.convertsampleproj.domain.model.PickerEnum
import com.example.convertsampleproj.domain.usecase.ConvertUseCase
import com.example.convertsampleproj.ui.contact.ItemListDialogFragment
import com.example.convertsampleproj.ui.contact.OnItemSelectedListener
import com.example.convertsampleproj.ui.modal.ConvertCustomDialog

/**
 * A simple [Fragment] subclass.
 * Use the [ConvertFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ConvertFragment : Fragment(), OnItemSelectedListener {
  //  binding 해주기 (뷰 바인딩 설정)
  private lateinit var binding: FragmentConvertBinding
  
  private lateinit var convertUseCase: ConvertUseCase
  private lateinit var convertViewModel: ConvertViewModel
  
  private lateinit var audioManager: AudioManager
  private lateinit var vibrator: Vibrator
  private lateinit var toneGenerator: ToneGenerator
  
  override fun onAttach(context: Context) {
    super.onAttach(context)
  }
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
  }
  
  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
  ): View? {
    // Inflate the layout for this fragment
    binding = FragmentConvertBinding.inflate(inflater, container, false)
    audioManager = requireActivity().getSystemService(android.content.Context.AUDIO_SERVICE) as AudioManager
    vibrator = requireContext().getSystemService(Vibrator::class.java)
    toneGenerator = ToneGenerator(AudioManager.STREAM_SYSTEM, 40)
    
    return binding.root
  }
  
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    
    convertUseCase = ConvertUseCase()
    convertViewModel = ViewModelProvider(this)[ConvertViewModel::class.java]

//    Picker 값 실시간 업데이트
    setValuePicker(convertViewModel, binding.npConvSelectType)
    
    //    단위 변환
    setUpListeners()
  }
  
  // 이벤트 따라 단위 변환 - ValuePicker 변환 시는 setValuePicker 함수에서 변환 실행
  private fun setUpListeners() {
    binding.tvResult.setOnClickListener {
//      showConvertModal("Result")
      showBottomSheet(R.id.tvResult)
    }
    
    binding.tvValue.setOnClickListener {
//      showConvertModal("Value")
      showBottomSheet(R.id.tvValue)
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
  @RequiresApi(Build.VERSION_CODES.Q)
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
//      audioManager.playSoundEffect(AudioManager.FX_KEY_CLICK)
//      view?.playSoundEffect(SoundEffectConstants.CLICK)
//      TONE_SUP_INTERCEPT
//      TONE_PROP_BEEP
//      toneGenerator.startTone(ToneGenerator.TONE_PROP_BEEP, 50)
      triggerVibration()
      
    }

//    Card의 Value 값 변환
    viewModel.text.observe(viewLifecycleOwner) {
      binding.tvConvCard.text = it
      performConversionUnit()
    }
  }
  
  @RequiresApi(Build.VERSION_CODES.Q)
  private fun triggerVibration() {
    if (vibrator.hasVibrator()) {
      try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
          // Android 8.0 (API 26) 이상의 경우
          val vibrationEffect = VibrationEffect.createOneShot(30, 20)
//          val vibrationEffect = VibrationEffect.createOneShot(100, VibrationEffect.EFFECT_TICK)
          vibrator.vibrate(vibrationEffect) // 100ms 동안 진동
        } else {
          // Android 8.0 이하에서 사용
          @Suppress("DEPRECATION")
          vibrator.vibrate(30) // 100ms 동안 진동
        }
      } catch (e: Exception) {
        e.printStackTrace() // 예외 처리
      }
    }
  }
  
  
  // [Function] Modal
//  private fun showConvertModal(type: String) {
//    val modalFragment = ConvertCustomDialog()
//    modalFragment.setOnConvertModalListener(this)
//    modalFragment.show(childFragmentManager, "ConvertModalFragment")
//  }
//
  //  [Function] Bottom Sheet ------
  private fun showBottomSheet(targetTextViewId: Int) {
    Log.d("showBottomSheet", "showBottomSheet: ${targetTextViewId}")
    
    val bottomSheet = ItemListDialogFragment()
    bottomSheet.setOnItemSelectedListener(this)
    val args = Bundle().apply {
      putInt("buttonId", targetTextViewId)
    }
    bottomSheet.arguments = args
    bottomSheet.show(parentFragmentManager, "ItemListDialogFragment")
  }
  
  override fun onItemSelected(item: String, targetTextViewId: Int) {
    Log.d("onItemSelected", "onItemSelected: ${item}, ${targetTextViewId} ${R.id.tvResult} ${R.id.tvValue}")
    when (targetTextViewId) {
      R.id.tvResult -> binding.tvRstMainUnit.text = item
      R.id.tvValue -> binding.tvMainUnit.text = item
    }
    performConversionUnit()
  }
  
  
  // [Function] Bottom Sheet -----

//  override fun OnSelectConvertUnit(selectedUnit: String, enteredValue: Double) {
//    performConversionUnit()
//  }
  
}