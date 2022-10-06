package com.github.adamr22.timer.data.models

import android.os.CountDownTimer
import android.os.Parcel
import android.os.Parcelable
import com.github.adamr22.timer.presentation.viewmodels.TimerViewModel
import java.util.*

data class TimerModel(
    var setTime: Calendar,
    var label: String? = null,
    var timerState: TimerViewModel.TimerStates = TimerViewModel.TimerStates.RUNNING,
    var timer: CountDownTimer? = null,
    val timerId: Int = Math.random().toInt(),
    var timeRemaining: Long = TimerViewModel().convertTimeToMilliseconds(setTime)
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readSerializable() as Calendar,
        parcel.readString(),
        TimerViewModel.TimerStates.values()[parcel.readInt()],
        null,
        parcel.readInt(),
        parcel.readLong()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeSerializable(setTime)
        parcel.writeString(label)
        parcel.writeInt(timerState.ordinal)
        parcel.writeInt(timerId)
        parcel.writeLong(timeRemaining)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TimerModel> {
        override fun createFromParcel(parcel: Parcel): TimerModel {
            return TimerModel(parcel)
        }

        override fun newArray(size: Int): Array<TimerModel?> {
            return arrayOfNulls(size)
        }
    }
}
