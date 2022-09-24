package com.github.adamr22.timer.presentation.adapters

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.github.adamr22.timer.presentation.views.RunTimerViewFragment
import com.github.adamr22.timer.presentation.viewmodels.TimerViewModel
import kotlinx.coroutines.flow.collectLatest

class RunFragmentViewPagerAdapter(
    private val mFragmentManager: FragmentManager,
    fa: Fragment,
    val viewModel: TimerViewModel,
    val context: Context
) : FragmentStateAdapter(fa) {

    private val fragmentList = mutableListOf<RunTimerViewFragment>()

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        (context as AppCompatActivity).lifecycleScope.launchWhenCreated {
            viewModel.timers.collectLatest {
                fragmentList.clear()
                when (it) {
                    is TimerViewModel.TimerFragmentUIState.Timers -> {
                        it.timerInstances.forEach { timerModel ->
                            fragmentList.add(
                                RunTimerViewFragment.newInstance(
                                    timerModel,
                                    viewModel,
                                    mFragmentManager,
                                    this@RunFragmentViewPagerAdapter
                                )
                            )
                        }
                    }
                    is TimerViewModel.TimerFragmentUIState.Empty -> {
                        mFragmentManager.popBackStack()
                    }
                }
            }
        }
        super.onAttachedToRecyclerView(recyclerView)
    }

    override fun getItemCount(): Int {
        if (fragmentList.isEmpty()) mFragmentManager.popBackStack()
        return fragmentList.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragmentList[position].also {
            it.setPos(position)
        }
    }

    override fun getItemId(position: Int): Long {
        return fragmentList[position].hashCode().toLong()
    }

    override fun containsItem(itemId: Long): Boolean {
        return fragmentList.find { it.hashCode().toLong() == itemId } != null
    }

    fun removeItem(position: Int) {
        fragmentList.removeAt(position)
        notifyItemRemoved(position)
    }
}