package com.example.pkl_v1.service

import android.app.Service
import android.content.Intent
import android.os.IBinder

class SensorService:Service() {
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}

fun wait(ms: Int) {
    try {
        Thread.sleep(ms.toLong())
    } catch (ex: InterruptedException) {
        Thread.currentThread().interrupt()
    }
}