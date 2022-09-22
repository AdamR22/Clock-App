package com.github.adamr22.timer

import android.content.Context
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.adapter.FragmentStateAdapter
import kotlinx.coroutines.flow.collectLatest

open class RunFragmentViewPagerAdapter(
    private val mFragmentManager: FragmentManager,
    fa: Fragment,
    val viewModel: TimerViewModel,
    val context: Context
) : FragmentStateAdapter(fa) {

    private val TAG = "RunFragmentViewPagerAda"

    override fun getItemCount(): Int {
        var itemCount = 0
        (context as AppCompatActivity).lifecycleScope.launchWhenCreated {
            viewModel.timers.collectLatest {
                itemCount = when (it) {
                    is TimerViewModel.TimerFragmentUIState.Timers -> it.timerInstances.size
                    is TimerViewModel.TimerFragmentUIState.Empty -> 0
                }
            }
        }

        return itemCount
    }

    override fun createFragment(position: Int): Fragment {
        var fragment: RunTimerViewFragment? = null

        (context as AppCompatActivity).lifecycleScope.launchWhenCreated {
            viewModel.timers.collectLatest {
                when (it) {
                    is TimerViewModel.TimerFragmentUIState.Timers -> {
                        it.timerInstances.forEach { timerModel ->
                            val pos = it.timerInstances.indexOf(timerModel)
                            Log.d(TAG, "createFragment: frag position: $pos")
                            fragment = RunTimerViewFragment.newInstance(
                                timerModel,
                                pos,
                                viewModel,
                                mFragmentManager,
                                this@RunFragmentViewPagerAdapter
                            )
                        }

                    }
                    is TimerViewModel.TimerFragmentUIState.Empty -> {
                        mFragmentManager.popBackStack()
                    }
                }
            }
        }

        return fragment!!
    }

}