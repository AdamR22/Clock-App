package com.github.adamr22.timer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.adamr22.R
import ru.tinkoff.scrollingpagerindicator.ScrollingPagerIndicator

class RunTimerFragment : Fragment() {

    private val tempList = listOf(
        "hello",
        "twat",
        "ring",
        "the",
        "may ",
        "for",
        "eternity"
    )

    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mRecyclerViewIndicator: ScrollingPagerIndicator
    private lateinit var runTimerAdapter: RunTimerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_run_timer, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mRecyclerView = view.findViewById(R.id.rv_run_timer)
        runTimerAdapter = RunTimerAdapter(tempList, requireContext(), parentFragmentManager)
        mRecyclerViewIndicator = view.findViewById<ScrollingPagerIndicator?>(R.id.set_timer_indicator).also {
            it.visibleDotCount = tempList.size
        }

        mRecyclerView.adapter = runTimerAdapter
        mRecyclerViewIndicator.attachToRecyclerView(mRecyclerView)
        super.onViewCreated(view, savedInstanceState)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            RunTimerFragment()
    }
}