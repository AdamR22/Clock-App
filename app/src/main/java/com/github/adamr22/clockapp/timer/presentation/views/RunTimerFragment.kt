package com.github.adamr22.clockapp.timer.presentation.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.github.adamr22.clockapp.R
import com.github.adamr22.clockapp.timer.presentation.viewmodels.TimerViewModel
import com.github.adamr22.clockapp.timer.presentation.adapters.RunFragmentViewPagerAdapter
import ru.tinkoff.scrollingpagerindicator.ScrollingPagerIndicator

class RunTimerFragment : Fragment() {

    private lateinit var mViewPagerIndicator: ScrollingPagerIndicator

    private lateinit var viewPager: ViewPager2
    private lateinit var timerViewModel: TimerViewModel

    fun passViewModel(viewModel: TimerViewModel) {
        timerViewModel = viewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_run_timer, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (requireContext() as AppCompatActivity).supportActionBar?.title = "Timer"
        viewPager = view.findViewById(R.id.run_timer_layout_pager)
        val viewPagerAdapter = RunFragmentViewPagerAdapter(parentFragmentManager,this, timerViewModel, requireContext())
        viewPager.adapter = viewPagerAdapter
        mViewPagerIndicator = view.findViewById(R.id.set_timer_indicator)
        mViewPagerIndicator.attachToPager(viewPager)
        super.onViewCreated(view, savedInstanceState)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            RunTimerFragment()
    }

}