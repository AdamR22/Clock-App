package com.github.adamr22

import android.app.Activity
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.RecyclerView
import com.github.adamr22.alarm.presentation.adapters.AlarmRecyclerViewAdapter
import com.github.adamr22.alarm.presentation.viewmodels.AlarmViewModel
import com.github.adamr22.alarm.presentation.views.AlarmFragment
import com.github.adamr22.bedtime.BedTimeFragment
import com.github.adamr22.clock.ClockFragment
import com.github.adamr22.stopwatch.StopWatchFragment
import com.github.adamr22.timer.TimerFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity(), AlarmRecyclerViewAdapter.CallbackInterface {

    lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var alarmViewModel: AlarmViewModel
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mToolbar: Toolbar
    private var alarmItem = 0

    private val activityForResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK && it.data != null) {
                val data = it.data
                val ringtoneUri =
                    data?.getParcelableExtra<Uri>(RingtoneManager.EXTRA_RINGTONE_PICKED_URI)!!
                val ringtoneTitle = RingtoneManager.getRingtone(this, ringtoneUri)
                    .getTitle(this).toString()

                alarmViewModel.changeRingtone(alarmItem, ringtoneTitle, ringtoneUri)
                mRecyclerView.adapter!!.notifyItemChanged(alarmItem)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bottomNavigationView = findViewById(R.id.bottomNav)
        mToolbar = findViewById(R.id.app_toolbar)
        setSupportActionBar(mToolbar)
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
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, ClockFragment.newInstance()).commit()
                    true
                }
                R.id.timer -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, TimerFragment.newInstance()).commit()
                    true
                }
                R.id.stop_watch -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, StopWatchFragment.newInstance()).commit()
                    true
                }
                R.id.bed_time -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, BedTimeFragment.newInstance()).commit()
                    true
                }
                else -> false
            }
        }
    }

    override fun selectChosenTone(index: Int, viewModel: AlarmViewModel, recyclerView: RecyclerView) {
        alarmItem = index
        alarmViewModel = viewModel
        mRecyclerView = recyclerView
        Intent(RingtoneManager.ACTION_RINGTONE_PICKER).run {
            putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Alarm Sound")
            putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_ALARM)
            putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT, true)
            putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, true)
            activityForResultLauncher.launch(this)
        }
    }
}