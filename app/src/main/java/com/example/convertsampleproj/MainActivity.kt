package com.example.convertsampleproj

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.convertsampleproj.databinding.ActivityMainBinding
import com.example.convertsampleproj.ui.contact.ContactFragment
import com.example.convertsampleproj.ui.convert.ConvertFragment
import com.example.convertsampleproj.ui.disclaimer.DisclaimerFragment
import com.example.convertsampleproj.ui.table.TableFragment

class MainActivity : AppCompatActivity() {
  //  binding 해주기 (뷰 바인딩 설정)
  private val binding: ActivityMainBinding by lazy {
    ActivityMainBinding.inflate(layoutInflater)
  }
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContentView(binding.root)
    
    window.statusBarColor = resources.getColor(R.color.white, theme)
    setBottomNavigationView()
    
    //    하단 Navigation 바 추가, Padding 조절 (Bottom: 0)
    ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
      val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
      v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
      insets
    }
  }
  
  fun setBottomNavigationView() {
    binding.bottomNavigation.setOnItemSelectedListener { item ->
      when (item.itemId) {
        R.id.navigation_convert -> {
          supportFragmentManager.beginTransaction()
            .replace(R.id.main_container, ConvertFragment()).commit()
          true
        }
        
        R.id.navigation_table -> {
          supportFragmentManager.beginTransaction().replace(R.id.main_container, TableFragment())
            .commit()
          true
        }
        
        R.id.navigation_disclaimer -> {
          supportFragmentManager.beginTransaction()
            .replace(R.id.main_container, DisclaimerFragment()).commit()
          true
        }
        
        R.id.navigation_contact -> {
          supportFragmentManager.beginTransaction()
            .replace(R.id.main_container, ContactFragment()).commit()
          true
        }
        
        else -> false
      }
    }
  }
}