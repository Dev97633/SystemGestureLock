package com.dev.systemgesture.service

import android.accessibilityservice.AccessibilityService
import android.content.Intent
import android.view.accessibility.AccessibilityEvent
import com.dev.systemgesture.core.LockController
import com.dev.systemgesture.ui.SetupActivity

class GestureAccessibilityService : AccessibilityService() {

        private var lastTouchEndAt = 0L

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        val safeEvent = event ?: return
        val type = safeEvent.eventType
                if (type != AccessibilityEvent.TYPE_TOUCH_INTERACTION_END) {
            return
        }
         val now = safeEvent.eventTime
        val elapsed = now - lastTouchEndAt
        if (elapsed in DOUBLE_TAP_MIN_GAP_MS..DOUBLE_TAP_WINDOW_MS) {
            val locked = LockController.lock(this)
            if (!locked) {
                val setupIntent = Intent(this, SetupActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                startActivity(setupIntent)
            }

            lastTouchEndAt = 0L
            return
        }
        
        lastTouchEndAt = now
    }

    override fun onInterrupt() = Unit

    companion object {
        private const val DOUBLE_TAP_MIN_GAP_MS = 60L
        private const val DOUBLE_TAP_WINDOW_MS = 500L
    }
}
