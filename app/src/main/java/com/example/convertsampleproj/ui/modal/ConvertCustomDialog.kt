package com.example.convertsampleproj.ui.modal

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.example.convertsampleproj.R
import com.example.convertsampleproj.domain.model.LengthEnum

class ConvertCustomDialog : DialogFragment() {
  interface OnConvertModalListener {
    fun OnSelectConvertUnit(selectedUnit: String, enteredValue: Double)
  }
  
  private var listener: OnConvertModalListener? = null
  
  
  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(R.layout.convert_modal, container, false)
  }
  
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    
    val spinnerMainUnit: Spinner = view.findViewById(R.id.spConvModalMainUnit)
    val etConvModalSub: EditText = view.findViewById(R.id.etConvModalSub)
//    val tvTitle: TextView = view.findViewById(R.id.tvConvModalTitle)
//    val tvSubTitle: TextView = view.findViewById(R.id.tvConvModalSubTitle)
//    val tvSub: TextView = view.findViewById(R.id.tvConvModalSub)
    
    val btnOk: Button = view.findViewById(R.id.btnModalOK)
    val btnCancel: Button = view.findViewById(R.id.btnModalCancel)
    
    val lengthValues = LengthEnum.entries.toTypedArray().map { it.displayName }.toTypedArray()
    val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, lengthValues)
    spinnerMainUnit.adapter = adapter
    
    // OK 버튼 클릭 시 처리
    btnOk.setOnClickListener {
      val selectedUnit = spinnerMainUnit.selectedItem.toString()
      val enteredValue = etConvModalSub.text.toString().toDoubleOrNull() ?: 0.0
      listener?.OnSelectConvertUnit(selectedUnit, enteredValue)
      dismiss()
    }
    
    // Cancel 버튼 클릭 시 처리
    btnCancel.setOnClickListener {
      dismiss()
    }
    
  }
  
  fun setOnConvertModalListener(listener: OnConvertModalListener) {
    this.listener = listener
  }
}