package com.github.adamr22.bedtime

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.github.adamr22.R

class BedTimeFragment : Fragment() {

    companion object {
        fun newInstance() = BedTimeFragment()
    }

    private lateinit var viewModel: BedTimeViewModel

    private lateinit var tvBedtime: TextView
    private lateinit var tvWakeupTime: TextView

    val modalBottomSheet by lazy { BedTImeFragmentBottomSheet() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[BedTimeViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        (requireActivity() as AppCompatActivity).supportActionBar?.title = resources.getString(R.string.bedtime)

        return inflater.inflate(R.layout.fragment_bed_time, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvBedtime = view.findViewById(R.id.bedtime_alarm_time)
        tvWakeupTime = view.findViewById(R.id.wakeup_alarm_time)
    }

    override fun onResume() {
        super.onResume()

        tvBedtime.setOnClickListener { openModalSheet() }
        tvWakeupTime.setOnClickListener { openModalSheet() }
    }

    private fun openModalSheet() {
        modalBottomSheet.show(parentFragmentManager, modalBottomSheet.tag)
    }
}