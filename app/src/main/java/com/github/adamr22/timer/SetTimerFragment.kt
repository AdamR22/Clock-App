package com.github.adamr22.timer

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.github.adamr22.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.flow.collectLatest

class SetTimerFragment : Fragment() {

    private lateinit var timerViewModel: TimerViewModel

    private lateinit var btnStartTimer: FloatingActionButton
    private lateinit var timerAdapter: SetTimerAdapter
    private lateinit var timerNumpad: GridView

    private lateinit var tvHours: TextView
    private lateinit var tvMinutes: TextView
    private lateinit var tvSeconds: TextView

    private val TAG = "SetTimerFragment"

    private val timerNumpadText =
        arrayOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "00", "0", "X")

    private var setTime: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        timerAdapter = SetTimerAdapter(timerNumpadText, requireContext())
        timerViewModel = ViewModelProvider(requireActivity())[TimerViewModel::class.java]
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_set_timer, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        btnStartTimer = view.findViewById(R.id.start_timer)
        timerNumpad = view.findViewById(R.id.timer_numpad)

        tvHours = view.findViewById(R.id.tv_hours)
        tvMinutes = view.findViewById(R.id.tv_minutes)
        tvSeconds = view.findViewById(R.id.tv_seconds)

        timerNumpad.adapter = timerAdapter

        super.onViewCreated(view, savedInstanceState)
    }

    override fun onResume() {
        btnStartTimer.setOnClickListener {
            parentFragmentManager.beginTransaction().setCustomAnimations(
                androidx.appcompat.R.anim.abc_slide_in_top,
                androidx.appcompat.R.anim.abc_slide_out_bottom
            )
                .replace(R.id.timer_fragment_layout, RunTimerFragment.newInstance())
                .commit()
        }

        timerNumpad.setOnItemClickListener { _, _, position, _ ->
            if (timerNumpadText[position] == "X" && setTime.isNotEmpty()) {
                setTime = setTime.dropLast(1)
            }

            if (setTime.length < 6 && timerNumpadText[position] != "X") {
                setTime += timerNumpadText[position]
            }

            Log.d(TAG, "onResume: Timer: $setTime")

            changeTextViewColor(setTime)
            showBtn(setTime)

        }
        super.onResume()
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            SetTimerFragment()
    }

    private fun changeTextViewColor(input: String) {

        when (input.length) {
            0 -> tvSeconds.apply {
                this.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.grey_50
                    )
                )
                this.text = String.format(getString(R.string.timer_text, "00", "s"))
            }
            1 -> {
                tvSeconds.apply {
                    this.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.black
                        )
                    )
                    this.text =
                        String.format(
                            getString(
                                R.string.timer_text,
                                "0${input[0]}",
                                "s"
                            )
                        )
                }
            }
            2 -> {
                tvMinutes.apply {
                    this.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.grey_50
                        )
                    )
                    this.text = String.format(getString(R.string.timer_text, "00", "m"))
                }
                tvSeconds.apply {
                    this.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.black
                        )
                    )
                    this.text =
                        String.format(getString(R.string.timer_text, input, "s"))
                }
            }
            3 -> {
                tvMinutes.apply {
                    this.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.black
                        )
                    )
                    this.text =
                        String.format(
                            getString(
                                R.string.timer_text,
                                "0${input[0]}",
                                "m"
                            )
                        )
                }

                tvSeconds.apply {
                    this.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.black
                        )
                    )
                    this.text =
                        String.format(
                            getString(
                                R.string.timer_text,
                                input.slice(1..2),
                                "s"
                            )
                        )
                }

            }
            4 -> {
                tvHours.apply {
                    this.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.grey_50
                        )
                    )
                    this.text = String.format(getString(R.string.timer_text, "00", "h"))
                }
                tvMinutes.apply {
                    this.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.black
                        )
                    )
                    this.text =
                        String.format(
                            getString(
                                R.string.timer_text,
                                input.slice(0..1),
                                "m"
                            )
                        )
                }
                tvSeconds.apply {
                    this.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.black
                        )
                    )
                    this.text =
                        String.format(
                            getString(
                                R.string.timer_text,
                                input.slice(2..3),
                                "s"
                            )
                        )
                }
            }
            5 -> {
                tvHours.apply {
                    this.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.black
                        )
                    )
                    this.text =
                        String.format(
                            getString(
                                R.string.timer_text,
                                "0${input[0]}",
                                "h"
                            )
                        )
                }
                tvMinutes.apply {
                    this.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.black
                        )
                    )
                    this.text =
                        String.format(
                            getString(
                                R.string.timer_text,
                                input.slice(1..2),
                                "m"
                            )
                        )
                }
                tvSeconds.apply {
                    this.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.black
                        )
                    )
                    this.text =
                        String.format(
                            getString(
                                R.string.timer_text,
                                input.slice(3..4),
                                "s"
                            )
                        )
                }

            }
            6 -> {
                tvHours.apply {
                    this.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.black
                        )
                    )
                    this.text =
                        String.format(
                            getString(
                                R.string.timer_text,
                                input.slice(0..1),
                                "h"
                            )
                        )
                }
                tvMinutes.apply {
                    this.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.black
                        )
                    )
                    this.text =
                        String.format(
                            getString(
                                R.string.timer_text,
                                input.slice(2..3),
                                "m"
                            )
                        )
                }
                tvSeconds.apply {
                    this.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.black
                        )
                    )
                    this.text =
                        String.format(
                            getString(
                                R.string.timer_text,
                                input.slice(4..5),
                                "s"
                            )
                        )
                }
            }
        }


    }

    private fun showBtn(input: String) {
        btnStartTimer.visibility = if (input.isEmpty()) View.GONE else View.VISIBLE
    }

    private fun setTime() {}
}