package com.dev.systemgesture.service

import android.app.Service
import android.content.Intent
import android.graphics.PixelFormat
import android.os.IBinder
import android.view.View
import android.view.WindowManager
import com.dev.systemgesture.util.NotificationUtil

class OverlayService : Service() {

    private lateinit var wm: WindowManager
    private lateinit var view: View

    override fun onCreate() {
        super.onCreate()

        startForeground(1, NotificationUtil.create(this))

        wm = getSystemService(WINDOW_SERVICE) as WindowManager
        view = View(this)

        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            PixelFormat.TRANSLUCENT
        )
        
        wm.addView(view, params)
    }
    override fun onDestroy() {
        super.onDestroy()
        if (::view.isInitialized) {
            wm.removeView(view)
        }
    }
    override fun onBind(intent: Intent?): IBinder? = null
}
