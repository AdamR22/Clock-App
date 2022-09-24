package com.github.adamr22.timer.presentation.views

import android.os.Bundle
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
import com.github.adamr22.timer.presentation.viewmodels.TimerViewModel
import com.github.adamr22.timer.presentation.adapters.SetTimerAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.flow.collectLatest

class SetTimerFragment : Fragment() {

    private var setTime = ""

    private lateinit var timerViewModel: TimerViewModel

    private lateinit var btnStartTimer: FloatingActionButton
    private lateinit var btnDeleteTimer: FloatingActionButton
    private lateinit var timerAdapter: SetTimerAdapter
    private lateinit var timerNumpad: GridView

    private lateinit var tvHours: TextView
    private lateinit var tvMinutes: TextView
    private lateinit var tvSeconds: TextView

    private val timerNumpadText =
        arrayOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "00", "0", "X")

    companion object {
        fun newInstance() = SetTimerFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        timerAdapter = SetTimerAdapter(timerNumpadText, requireContext())
        timerViewModel = ViewModelProvider(requireActivity())[TimerViewModel::class.java]

        setTime = timerViewModel.setTime

        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.set_fragment_timer, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        btnDeleteTimer = view.findViewById(R.id.btn_delete_timer_instance)
        btnStartTimer = view.findViewById(R.id.play)
        timerNumpad = view.findViewById(R.id.timer_numpad)

        tvHours = view.findViewById(R.id.tv_hours)
        tvMinutes = view.findViewById(R.id.tv_minutes)
        tvSeconds = view.findViewById(R.id.tv_seconds)

        timerNumpad.adapter = timerAdapter

        changeTextViewColor(setTime)
        showBtn(setTime)

        super.onViewCreated(view, savedInstanceState)
    }

    override fun onResume() {

        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            timerViewModel.timers.collectLatest {
                when (it) {
                    is TimerViewModel.TimerFragmentUIState.Timers -> {
                        btnDeleteTimer.visibility =
                            if (it.timerInstances.isEmpty()) View.GONE else View.VISIBLE
                    }

                    else -> {}
                }
            }
        }

        btnDeleteTimer.setOnClickListener { navigateToRunTimerScreen() }

        btnStartTimer.setOnClickListener {
            setTime(setTime)
        }

        timerNumpad.setOnItemClickListener { _, _, position, _ ->
            if (timerNumpadText[position] == "X" && setTime.isNotEmpty()) {
                setTime = setTime.replace(setTime, "")
                tvHours.text = String.format(getString(R.string.timer_text, "00", "h"))
                tvMinutes.text = String.format(getString(R.string.timer_text, "00", "m"))
                tvSeconds.text = String.format(getString(R.string.timer_text, "00", "s"))
            }

            if (setTime.length < 6 && timerNumpadText[position] != "X") {
                setTime += timerNumpadText[position]
            }

            changeTextViewColor(setTime)
            showBtn(setTime)
        }

        super.onResume()
    }

    override fun onDestroy() {
        timerViewModel.setTime = setTime
        super.onDestroy()
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
                tvMinutes.apply {
                    this.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.grey_50
                        )
                    )
                }
                tvHours.apply {
                    this.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.grey_50
                        )
                    )
                }
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

    private fun setTime(input: String) {
        timerViewModel.addTimer(input)
        navigateToRunTimerScreen()
    }

    private fun navigateToRunTimerScreen() {
        timerViewModel.currentFragment = 1

        val runTimerFrag = RunTimerFragment.newInstance().apply {
            this.passViewModel(timerViewModel)
        }

        parentFragmentManager.beginTransaction()
            .replace(R.id.timer_fragment_layout, runTimerFrag).addToBackStack(null).commit()
    }

}