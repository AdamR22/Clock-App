package com.github.adamr22.bedtime.presentation.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.github.adamr22.R
import com.github.adamr22.bedtime.presentation.viewmodels.BedTimeViewModel
import com.github.adamr22.data.entities.Alarm
import com.github.adamr22.data.models.AlarmItemModel
import kotlinx.coroutines.flow.collectLatest

class BedTimeFragment : Fragment() {

    companion object {
        fun newInstance() = BedTimeFragment()
    }

    private val BEDTIME_TAG = "Bedtime"
    private val WAKEUP_TAG = "Wakeup"

    private var BEDTIME_LABEL = ""
    private var WAKEUP_LABEL = ""

    private val inflateBedTimeLayout = true
    private val inflateWakeUpLayout = true

    private val viewModel by lazy {
        ViewModelProvider(requireActivity())[BedTimeViewModel::class.java]
    }

    private lateinit var tvBedTimeLabel: TextView
    private lateinit var tvWakeUpLabel: TextView

    private lateinit var tvBedtime: TextView
    private lateinit var tvWakeupTime: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        (requireActivity() as AppCompatActivity).supportActionBar?.title =
            resources.getString(R.string.bedtime)

        BEDTIME_LABEL = resources.getString(R.string.bedtime_capitalized)
        WAKEUP_LABEL = resources.getString(R.string.wake_up)

        return inflater.inflate(R.layout.fragment_bed_time, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvBedTimeLabel = view.findViewById(R.id.bedtime_alarm_label)
        tvWakeUpLabel = view.findViewById(R.id.wakeup_alarm_label)

        tvBedtime = view.findViewById<TextView?>(R.id.bedtime_alarm_time).also {
            it.text = resources.getString(R.string.default_time)
        }

        tvWakeupTime = view.findViewById<TextView?>(R.id.wakeup_alarm_time).also {
            it.text = resources.getString(R.string.default_time)
        }
    }

    override fun onResume() {
        super.onResume()

        getData()

        tvBedtime.setOnClickListener { openModalSheet(true) }
        tvWakeupTime.setOnClickListener { openModalSheet(false) }
    }

    private fun openModalSheet(isBedTime: Boolean) {
        val bundle = Bundle().also {
            if (isBedTime) it.putBoolean(BEDTIME_TAG, inflateBedTimeLayout) else it.putBoolean(
                WAKEUP_TAG,
                inflateWakeUpLayout
            )
        }

        val bottomModalSheet = BedTImeFragmentBottomSheet().also {
            it.arguments = bundle
        }

        bottomModalSheet.show(parentFragmentManager, BedTImeFragmentBottomSheet.TAG)
    }

    private fun getData() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.getBedtime(BEDTIME_LABEL).collectLatest {
                it?.let {
                    val item = encapsulateData(it)
                    changeBedtimeTextColor(item.isScheduled)
                    renderBedtimeText(item.hour, item.minute)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.getWakeup(WAKEUP_LABEL).collectLatest {
                it?.let {
                    val item = encapsulateData(it)
                    changeWakeupTextColor(item.isScheduled)
                    renderWakeupText(item.hour, item.minute)
                }
            }
        }
    }

    private fun changeWakeupTextColor(scheduled: Boolean) {
        changeTextColor(tvWakeUpLabel, tvWakeupTime, scheduled = scheduled)
    }

    private fun changeBedtimeTextColor(scheduled: Boolean) {
        changeTextColor(tvBedTimeLabel, tvBedtime, scheduled = scheduled)
    }

    private fun changeTextColor(vararg textViews: TextView, scheduled: Boolean) {
        if (scheduled) {
            textViews.forEach {
                it.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            }
        }

        if (!scheduled) {
            textViews.forEach {
                it.setTextColor(ContextCompat.getColor(requireContext(), R.color.grey))
            }
        }
    }

    private fun renderWakeupText(hour: Int, minute: Int) {
        renderViewText(tvWakeupTime, hour, minute)
    }

    private fun renderBedtimeText(hour: Int, minute: Int) {
        renderViewText(tvBedtime, hour, minute)
    }

    private fun renderViewText(textView: TextView, hour: Int, minute: Int) {
        textView.text = String.format(
            resources.getString(R.string.default_time_2),
            "%02d".format(hour),
            "%02d".format(minute)
        )
    }

    private fun encapsulateData(it: Alarm): AlarmItemModel {

        return AlarmItemModel(
            it.id!!,
            it.label,
            it.hour,
            it.minute,
            it.title,
            it.uri,
            it.isScheduled,
            it.vibrates,
            it.sunriseMode,
            it.expandedItem,
            it.everyDay
        )
    }

}