package com.github.adamr22.timer
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.github.adamr22.R

class TimerFragment : Fragment() {

    companion object {
        fun newInstance() = TimerFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_timer, container, false)
    }

    override fun onResume() {
        (activity as AppCompatActivity).supportActionBar?.title = "Timer"
        super.onResume()
    }

}