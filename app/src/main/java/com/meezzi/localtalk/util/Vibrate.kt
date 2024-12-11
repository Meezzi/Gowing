package com.meezzi.localtalk.util

import android.content.Context
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager

fun vibrate(context: Context) {
    val vibrator = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
        val vibratorManager =
            context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
        vibratorManager.defaultVibrator
    } else {
        context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    }
    vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE))
}
