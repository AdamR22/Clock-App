package com.github.adamr22

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.github.adamr22.alarm.presentation.views.AlarmFragment
import com.github.adamr22.bedtime.BedTimeFragment
import com.github.adamr22.clock.ClockFragment
import com.github.adamr22.stopwatch.StopWatchFragment
import com.github.adamr22.timer.TimerFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bottomNavigationView = findViewById(R.id.bottomNav)
    }

    override fun onResume() {
        super.onResume()
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.alarm -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, AlarmFragment.newInstance()).commit()
                    true
                }
                R.id.clock -> {
                    supportFragmentManager.beginTransaction().replace(R.id.fragment_container, ClockFragment.newInstance()).commit()
                    true
                }
                R.id.timer -> {
                    supportFragmentManager.beginTransaction().replace(R.id.fragment_container, TimerFragment.newInstance()).commit()
                    true
                }
                R.id.stop_watch -> {
                    supportFragmentManager.beginTransaction().replace(R.id.fragment_container, StopWatchFragment.newInstance()).commit()
                    true
                }
                R.id.bed_time -> {
                    supportFragmentManager.beginTransaction().replace(R.id.fragment_container, BedTimeFragment.newInstance()).commit()
                    true
                }
                else -> false
            }
        }
    }
}