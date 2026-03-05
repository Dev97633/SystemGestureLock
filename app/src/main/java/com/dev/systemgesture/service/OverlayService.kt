package com.dev.systemgesture.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.dev.systemgesture.util.NotificationUtil

class OverlayService : Service() {
    override fun onCreate() {
        super.onCreate()

        startForeground(1, NotificationUtil.create(this))
    }
    override fun onBind(intent: Intent?): IBinder? = null
}
