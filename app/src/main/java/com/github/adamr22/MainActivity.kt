package com.github.adamr22

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.github.adamr22.alarm.presentation.viewmodels.AlarmViewModel
import com.github.adamr22.alarm.presentation.views.AlarmFragment
import com.github.adamr22.bedtime.presentation.views.BedTimeFragment
import com.github.adamr22.clock.ClockFragment
import com.github.adamr22.data.entities.AlarmAndDay
import com.github.adamr22.stopwatch.StopWatchFragment
import com.github.adamr22.timer.presentation.views.TimerFragment
import com.github.adamr22.utils.PickAlarmInterface
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.flow.collectLatest
import java.util.*

class MainActivity : AppCompatActivity(), PickAlarmInterface {

    lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var mToolbar: Toolbar
    private var currentFragment = 0

    private val FRAGMENT_ID = "frag_id"
    private val FRAG_KEY = "shared_pref_frag_id_key"

    private val ALARM_ID_TAG = "alarm_id"

    private var selectedTone: Pair<Uri, String>? = null

    private val viewModel by lazy {
        ViewModelProvider(this)[AlarmViewModel::class.java]
    }

    private val alarmManager by lazy {
        getSystemService(Context.ALARM_SERVICE) as AlarmManager
    }

    private val activityForResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK && it.data != null) {
                val data = it.data
                val ringtoneUri =
                    data?.getParcelableExtra<Uri>(RingtoneManager.EXTRA_RINGTONE_PICKED_URI)!!
                val ringtoneTitle = RingtoneManager.getRingtone(this, ringtoneUri)
                    .getTitle(this).toString()

                selectedTone = Pair(ringtoneUri, ringtoneTitle)

                returnSelectedTone()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            getSharedPreferences(FRAG_KEY, MODE_PRIVATE).apply {
                currentFragment = this.getInt(FRAGMENT_ID, 0)
            }
        }

        savedInstanceState?.let {
            currentFragment = it.getInt(FRAGMENT_ID)
        }

        bottomNavigationView = findViewById(R.id.bottomNav)
        mToolbar = findViewById(R.id.app_toolbar)
        setSupportActionBar(mToolbar)
    }

    override fun onResume() {
        super.onResume()

        when (currentFragment) {
            0 -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, AlarmFragment.newInstance()).commit()

                bottomNavigationView.selectedItemId = R.id.alarm
            }
            1 -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, ClockFragment.newInstance()).commit()

                bottomNavigationView.selectedItemId = R.id.clock
            }
            2 -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, TimerFragment.newInstance()).commit()

                bottomNavigationView.selectedItemId = R.id.timer
            }
            3 -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, StopWatchFragment.newInstance()).commit()

                bottomNavigationView.selectedItemId = R.id.stop_watch
            }
            4 -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, BedTimeFragment.newInstance()).commit()

                bottomNavigationView.selectedItemId = R.id.bed_time
            }
        }

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.alarm -> {
                    currentFragment = 0
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, AlarmFragment.newInstance()).commit()
                    true
                }
                R.id.clock -> {
                    currentFragment = 1
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, ClockFragment.newInstance()).commit()
                    true
                }
                R.id.timer -> {
                    currentFragment = 2
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, TimerFragment.newInstance()).commit()
                    true
                }
                R.id.stop_watch -> {
                    currentFragment = 3
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, StopWatchFragment.newInstance()).commit()
                    true
                }
                R.id.bed_time -> {
                    currentFragment = 4
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, BedTimeFragment.newInstance()).commit()
                    true
                }
                else -> false
            }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.getData().collectLatest {
                it?.let {
                    it.forEach { data ->
                        if (data.alarm.isScheduled) {
                            setAlarm(data)
                        }
                    }
                }
            }
        }
    }

    override fun selectAlarmTone() {
        Intent(RingtoneManager.ACTION_RINGTONE_PICKER).run {
            putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Alarm Sound")
            putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_ALARM)
            putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT, true)
            putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, true)
            activityForResultLauncher.launch(this)
        }
    }

    override fun returnSelectedTone(): Pair<Uri, String>? {
        return selectedTone
    }

    override fun onDestroy() {
        val sharedPref = getSharedPreferences(FRAG_KEY, MODE_PRIVATE)

        sharedPref.edit().also {
            it.putInt(FRAGMENT_ID, currentFragment)
            it.apply()
        } // Save most recent fragment after session ends

        super.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(FRAGMENT_ID, currentFragment)// Save most recent fragment in current session
        super.onSaveInstanceState(outState)
    }

    private fun setAlarm(
        data: AlarmAndDay
    ) {
        val alarmData = data.alarm
        val listOfSchedule = data.dayOfWeek

        val setHour: Int = alarmData.hour
        val setMinute: Int = alarmData.minute

        val setTime = Calendar.getInstance().apply {
            this.set(Calendar.HOUR_OF_DAY, setHour)
            this.set(Calendar.MINUTE, setMinute)
            this.set(Calendar.SECOND, 0)
        }

        if (listOfSchedule.isEmpty()) {
            if (setTime.before(Calendar.getInstance())) {
                // If set time is before time alarm was set by user, set alarm to be triggered day after
                setTime.add(Calendar.DAY_OF_WEEK, 1)
            }
            setAlarmTrigger(data.alarm.id!!, setTime)
        }

        if (listOfSchedule.isNotEmpty()) {
            listOfSchedule.forEach {
                if (it.day == Calendar.getInstance()
                        .getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault())
                ) {

                }
            }
        }
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private fun setAlarmTrigger(id: Int, timeInstance: Calendar) {

        val alarmRequestCode = (id + 1) * 1000

        val alarmIntent = Intent(this, WakeUpScreen::class.java).apply {
            this.putExtra(ALARM_ID_TAG, id)
        }

        val alarmPendingIntent = PendingIntent.getActivity(this, alarmRequestCode, alarmIntent, 0)

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            timeInstance.timeInMillis,
            alarmPendingIntent
        )
    }
}