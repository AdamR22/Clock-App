package com.github.adamr22.clock

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextClock
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.github.adamr22.R
import java.util.*

class ClockFragment : Fragment() {

    private lateinit var timeText: TextClock
    private lateinit var dateText: TextView
    private lateinit var calenderInstance: Calendar

    companion object {
        fun newInstance() = ClockFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (requireActivity() as AppCompatActivity).supportActionBar?.title = resources.getString(R.string.clock)
        return inflater.inflate(R.layout.fragment_clock, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        calenderInstance = Calendar.getInstance()
        timeText = view.findViewById(R.id.time)
        dateText = view.findViewById<TextView?>(R.id.date).also {
            it.text =
                "${calenderInstance.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault())}, ${calenderInstance.get(Calendar.DATE)} ${
                    calenderInstance.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault())
                }"
        }
        super.onViewCreated(view, savedInstanceState)
    }
}