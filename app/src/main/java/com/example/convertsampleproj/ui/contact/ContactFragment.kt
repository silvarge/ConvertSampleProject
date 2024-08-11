package com.example.convertsampleproj.ui.contact

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.convertsampleproj.R
import com.example.convertsampleproj.databinding.FragmentContactBinding
import kotlin.math.log

/**
 * A simple [Fragment] subclass.
 * Use the [ContactFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ContactFragment : Fragment() {
  private lateinit var binding: FragmentContactBinding
  private var textViewUpdate: Int = R.id.result // default
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

  }
  
  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    binding = FragmentContactBinding.inflate(inflater, container, false)
//    contactViewModel = ViewModelProvider(requireActivity())[ContactViewModel::class.java]
//
//    contactViewModel.selectedItem.observe(viewLifecycleOwner, Observer { item ->
//      updateResultText(item)
//    })
    
    binding.button.setOnClickListener {
      textViewUpdate = R.id.result
      showBottomSheet()
    }
    
    binding.button1.setOnClickListener {
      textViewUpdate = R.id.result1
      showBottomSheet()
    }
    
    // Inflate the layout for this fragment
    return binding.root
  }
  
  private fun showBottomSheet(){
    val bottomSheet = ItemListDialogFragment()
    bottomSheet.show(parentFragmentManager, "ItemListDialogFragment")
  }
  
  private fun updateResultText(result: String){
    Log.d("ContactFragment", "updateResultText: $result")
    when(textViewUpdate){
      R.id.result -> binding.result.text = result
      R.id.result1 -> binding.result1.text = result
    }
  }

}