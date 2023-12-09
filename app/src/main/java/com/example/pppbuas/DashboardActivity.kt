package com.example.pppbuas

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.pppbuas.databinding.ActivityDashboardBinding

class DashboardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        title = "KotlinRails"
        supportActionBar?.show()

        with(binding){
            val navController = findNavController(R.id.nav_host_fragment)
            bottomNavView.setupWithNavController(navController)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_options, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId){
            R.id.action_logout -> {
                val intentToMain = Intent(this@DashboardActivity, MainActivity::class.java)
                startActivity(intentToMain)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}