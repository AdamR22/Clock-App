package com.github.adamr22.clockapp.timer.presentation.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.github.adamr22.clockapp.R

class TimerFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_timer, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (requireContext() as AppCompatActivity).supportActionBar?.title = resources.getString(R.string.timer)
        super.onViewCreated(view, savedInstanceState)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            TimerFragment()
    }
}