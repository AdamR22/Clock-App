package com.github.adamr22.timer.presentation.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.github.adamr22.R
import com.github.adamr22.timer.presentation.viewmodels.TimerViewModel

class TimerFragment : Fragment() {

    private lateinit var viewModel: TimerViewModel
    private var currentFragment = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        viewModel = ViewModelProvider(requireActivity())[TimerViewModel::class.java]
        currentFragment = viewModel.currentFragment
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_timer, container, false)
    }

    override fun onResume() {
        if (currentFragment == 0) parentFragmentManager.beginTransaction()
            .replace(R.id.timer_fragment_layout, SetTimerFragment.newInstance())

        if (currentFragment == 1) parentFragmentManager.beginTransaction()
            .replace(R.id.timer_fragment_layout, RunTimerFragment.newInstance())

        super.onResume()
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            TimerFragment()
    }
}