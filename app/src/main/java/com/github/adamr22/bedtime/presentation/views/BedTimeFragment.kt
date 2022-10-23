package com.github.adamr22.bedtime.presentation.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.github.adamr22.R
import com.github.adamr22.bedtime.presentation.viewmodels.BedTimeViewModel

class BedTimeFragment : Fragment() {

    companion object {
        fun newInstance() = BedTimeFragment()
    }

    private val BEDTIME_TAG = "Bedtime"
    private val WAKEUP_TAG = "Wakeup"

    private val inflateBedTimeLayout = true
    private val inflateWakeUpLayout = true

    private lateinit var viewModel: BedTimeViewModel

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

        val bottomModalSheet = BedTImeFragmentBottomSheet(viewModel).also {
            it.arguments = bundle
        }

        bottomModalSheet.show(parentFragmentManager, BedTImeFragmentBottomSheet.TAG)
    }

}