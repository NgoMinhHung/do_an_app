package com.minhhung.life_app.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.minhhung.life_app.R
import com.minhhung.life_app.fragments.HomeFragment
import com.minhhung.life_app.fragments.SettingFragment
import com.minhhung.life_app.fragments.StatisticsFragment
import com.minhhung.life_app.utils.deleteAuthInformation
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val homeFragment = HomeFragment()

    private val statisticsFragment = StatisticsFragment()

    private val settingFragment = SettingFragment()

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomBar.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.actionHome -> {
                    showFragment(homeFragment)
                    true
                }

                R.id.actionStatistics -> {
                    showFragment(statisticsFragment)
                    true
                }

                R.id.actionSetting -> {
                    showFragment(settingFragment)
                    true
                }

                else -> false
            }
        }

        showFragment(homeFragment)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        return true
    }

    private fun showFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.container, fragment)
            addToBackStack("")
            commit()
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.item_log_out -> {
                Toast.makeText(
                    this,
                    getString(R.string.log_out_success_message),
                    Toast.LENGTH_SHORT
                ).show()
                doLogout()
                true
            }
            R.id.item_profile -> {
                showProfile()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showProfile() {
        startActivity(Intent(this, ProfileActivity::class.java))
    }

    private fun doLogout() {
        deleteAuthInformation()
        startActivity(Intent(this, SplashActivity::class.java))
        finish()
    }

}
