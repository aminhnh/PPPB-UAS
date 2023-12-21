package com.example.pppbuas.dashboard

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.pppbuas.MainActivity
import com.example.pppbuas.R
import com.example.pppbuas.databinding.ActivityDashboardBinding
import com.example.pppbuas.user.UserManager

class DashboardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)


        with(binding){
            val userRole = UserManager.getUserRole {
                userRole ->
                if (userRole != null) {
                    when (userRole) {
                        "user" -> {
                            title = "KotlinRails"
                            bottomNavView.menu.clear()
                            bottomNavView.inflateMenu(R.menu.bottom_navigation_user)
                        }
                        "admin" -> {
                            title = "Admin Panel"
                            bottomNavView.menu.clear()
                            bottomNavView.inflateMenu(R.menu.bottom_navigation_admin)
                        }
                    }
                } else {
                    // Failed to retrieve user role
                }
            }

            supportActionBar?.show()

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
            R.id.action_notification -> {
                //TODO: intent to notification
//                val intentToMain = Intent(this@DashboardActivity, MainActivity::class.java)
//                startActivity(intentToMain)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}