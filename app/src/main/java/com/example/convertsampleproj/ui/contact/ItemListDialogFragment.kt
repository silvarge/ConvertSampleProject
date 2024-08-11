package com.example.convertsampleproj.ui.contact

import android.os.Bundle
import android.util.Log
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.example.convertsampleproj.R
import com.example.convertsampleproj.databinding.FragmentItemListDialogListDialogItemBinding
import com.example.convertsampleproj.databinding.FragmentItemListDialogListDialogBinding
import com.example.convertsampleproj.ui.convert.ConvertUnitViewModel
import com.example.convertsampleproj.ui.convert.ConvertViewModel

// TODO: Customize parameter argument names
const val ARG_ITEM_COUNT = 10

/**
 *
 * A fragment that shows a list of items as a modal bottom sheet.
 *
 * You can show this modal bottom sheet from your activity like this:
 * <pre>
 *    ItemListDialogFragment.newInstance(30).show(supportFragmentManager, "dialog")
 * </pre>
 */

interface OnItemSelectedListener{
  fun onItemSelected(item: String, targetTextViewId: Int)
}

class ItemListDialogFragment : BottomSheetDialogFragment() {
  private lateinit var listener: OnItemSelectedListener
  
  fun setOnItemSelectedListener(listener: OnItemSelectedListener) {
    this.listener = listener
  }
  
  private var _binding: FragmentItemListDialogListDialogBinding? = null
  
  // This property is only valid between onCreateView and
  // onDestroyView.
  private val binding get() = _binding!!
//  private lateinit var contactViewModel: ContactViewModel
  private lateinit var convertUnitViewModel: ConvertUnitViewModel
  private lateinit var convertViewModel: ConvertViewModel
  
  
  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
  ): View? {
    
    _binding = FragmentItemListDialogListDialogBinding.inflate(inflater, container, false)
    convertUnitViewModel = ViewModelProvider(requireActivity())[ConvertUnitViewModel::class.java]
    convertViewModel = ViewModelProvider(this)[ConvertViewModel::class.java]
    return binding.root
    
  }
  
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//    binding.list.layoutManager = GridLayoutManager(context, 1)
    // 1, 2, 3, 4, 5를 데이터로 추가
    super.onViewCreated(view, savedInstanceState)
    
//    number picker 수정
    var items = listOf("귤", "바나나", "무화과", "복숭아", "사과")
    
    binding.numberPicker.minValue = 0
    binding.numberPicker.maxValue = items.size - 1
    binding.numberPicker.displayedValues = items.toTypedArray()
    
    // Adapter에 데이터 전달
//    binding.list.adapter = ItemAdapter(items!!)
    
    // EditText 설정 (필요시)
    binding.editText1.hint = "Search or type here... >.0"
    
    binding.confirmButton.setOnClickListener {
      
      val selectedValue = binding.numberPicker.value
      val selectedItem = items[selectedValue]
//      convertViewModel.updateSelectedItem(selectedValue)
      // Check if listener is set before calling the method
      val targetTextViewId = arguments?.getInt("buttonId")
      
//      여기 null만 ㅇ어케좀
      if (targetTextViewId != null) {
        listener.onItemSelected(selectedItem, targetTextViewId)
      }
      dismiss()
    }
  }
  
  
//  private inner class ViewHolder internal constructor(binding: FragmentItemListDialogListDialogItemBinding) :
//      RecyclerView.ViewHolder(binding.root) {
//        internal val text: TextView = binding.text
//      }
//
//  private inner class ItemAdapter internal constructor(private val mItemCount: List<String>) :
//      RecyclerView.Adapter<ViewHolder>() {
//
//        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//
//          return ViewHolder(
//            FragmentItemListDialogListDialogItemBinding.inflate(
//              LayoutInflater.from(
//                parent.context
//              ), parent, false
//            )
//          )
//      }
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//      holder.text.text = position.toString()
//    }
//
//    override fun getItemCount(): Int {
//      return items?.size ?: 0
//    }
//  }
//
//  companion object {
//
//    // TODO: Customize parameters
//    fun newInstance(itemCount: Int): ItemListDialogFragment = ItemListDialogFragment().apply {
//      arguments = Bundle().apply {
//        putInt(ARG_ITEM_COUNT.toString(), itemCount)
//      }
//    }
//
//  }
//
//  override fun onDestroyView() {
//    super.onDestroyView()
//    _binding = null
//  }


}