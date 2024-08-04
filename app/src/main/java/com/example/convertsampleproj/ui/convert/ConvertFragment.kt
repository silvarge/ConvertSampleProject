package com.example.convertsampleproj.ui.convert

import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.NumberPicker
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.convertsampleproj.R
import com.example.convertsampleproj.databinding.FragmentConvertBinding

/**
 * A simple [Fragment] subclass.
 * Use the [ConvertFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ConvertFragment : Fragment() {
  //  binding 해주기 (뷰 바인딩 설정)
  private lateinit var binding: FragmentConvertBinding
  private lateinit var audioManager: AudioManager
  private lateinit var mediaPlayer: MediaPlayer
  
  override fun onAttach(context: Context) {
    super.onAttach(context)
    audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    mediaPlayer = MediaPlayer.create(context, R.raw.test_audio)
  }
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
  }
  
  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
  ): View? {
    // Inflate the layout for this fragment
    binding = FragmentConvertBinding.inflate(inflater, container, false)
    val convertViewModel = ViewModelProvider(this)[ConvertViewModel::class.java]

//    Picker 값 실시간 업데이트
    setValuePicker(convertViewModel, binding.npConvSelectType, audioManager)
//    convertViewModel.updatePickerValues(PickerValue.entries.toTypedArray())
//    단위 변환
    convertUnit(binding.etBeforeValue, binding.etRstValue)
    
    return binding.root
  }
  
  //  [Function] 단위 변환
  private fun convertUnit(etBeforeValue: EditText, etAfterValue: EditText) {
    etBeforeValue.addTextChangedListener(object : TextWatcher {
      //      Text가 변화했을 때 감지
      override fun afterTextChanged(s: Editable?) {
//        Text가 변화하면 값을 가공하여 etResult에 넣음
        val value = s.toString().toDoubleOrNull()
        if (value != null) {
//           Value 값이 null이 아닌 숫자(Double)값 일 때 로직 수행
          val result = value * 0.1
          etAfterValue.setText(result.toString())
        } else {
//          Null이 입력되었을 때 혹은 예외 사항 생겼을 때
          etAfterValue.setText("Not A Number!")
        }
      }
      
      //      Override 필요로 선언만 해 둠
      override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
      }
      
      override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
      }
    })
  }
  
//  [Function] Picker 업데이트에 따른 Card 값 업데이트
  private fun setValuePicker(
  viewModel: ConvertViewModel, npValuePicker: NumberPicker, audioManager: AudioManager
  ) {
    viewModel.pickerValues.observe(viewLifecycleOwner) { values ->
      npValuePicker.minValue = 0
      npValuePicker.maxValue = values.size - 1
      npValuePicker.displayedValues = values
      npValuePicker.wrapSelectorWheel = true
    }

//    TODO: 효과음 부분
//    npValuePicker.setOnScrollListener { _, scrollState ->
//      Log.d("ConvertFragment", "onScrollStateChange: scrollState = $scrollState")
//      if (scrollState == NumberPicker.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
//        Log.d("ConvertFragment", "Playing click sound")
////        playSound()
////        playClickSound()
//      }
//    }

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
    }
    
  }
  
  // TODO: 효과음 부분 추후에 확인할 필요성 있음
//
//  private fun playClickSound() {
//    // 오디오 포커스 요청
//    val result = audioManager.requestAudioFocus(
//      null,
//      AudioManager.STREAM_MUSIC,
//      AudioManager.AUDIOFOCUS_GAIN_TRANSIENT
//    )
//    if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
//      Log.d("ConvertFragment", "Audio focus granted")
//      // 볼륨 설정
//      audioManager.setStreamVolume(
//        AudioManager.STREAM_MUSIC,
//        audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC),
//        0
//      )
//      // 소리 재생
//      audioManager.playSoundEffect(AudioManager.FX_KEY_CLICK)
//    } else {
//      Log.d("ConvertFragment", "Audio focus request failed")
//    }
//  }
//
//  private fun playSound() {
//    if (::mediaPlayer.isInitialized) {
//      mediaPlayer.start()
//    } else {
//      Log.d("ConvertFragment", "MediaPlayer not initialized")
//    }
//  }

}