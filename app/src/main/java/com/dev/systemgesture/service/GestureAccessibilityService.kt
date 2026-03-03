package com.dev.systemgesture.service

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent
import com.dev.systemgesture.core.LockController

class GestureAccessibilityService : AccessibilityService() {

    private var lastTapAt = 0L
override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event?.eventType != AccessibilityEvent.TYPE_TOUCH_INTERACTION_START) {
            return
        }

        val now = System.currentTimeMillis()
    if (now - lastTapAt < DOUBLE_TAP_WINDOW_MS) {
            LockController.lock(this)
            lastTapAt = 0L
            return
        }
    lastTapAt = now
    }
            override fun onInterrupt() {}
    
    companion object {
        private const val DOUBLE_TAP_WINDOW_MS = 300L
    }
}
