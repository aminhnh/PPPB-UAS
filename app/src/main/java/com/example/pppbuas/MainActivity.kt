package com.example.pppbuas

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.example.pppbuas.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    companion object {
        lateinit var viewPager2: ViewPager2
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        title = ""

        with(binding){
            viewPager.adapter = TabAdapter(this@MainActivity)
            viewPager2 = viewPager

            TabLayoutMediator(tabLayout, viewPager){
                    tab, position ->
                tab.text = when(position){
                    0 -> "Register"
                    1 -> "Login"
                    else -> ""
                }
            }.attach()

            tabLayout.isTabIndicatorFullWidth = true

            val intentToMain = Intent(this@MainActivity, DashboardActivity::class.java)
            startActivity(intentToMain)

        }
    }
}