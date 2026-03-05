package com.dev.systemgesture.service

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent
import com.dev.systemgesture.core.LockController

class GestureAccessibilityService : AccessibilityService() {

    private var lastTapAt = 0L
    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        val type = event?.eventType ?: return
    if (type != AccessibilityEvent.TYPE_TOUCH_INTERACTION_END) {
            return
        }

        val now = System.currentTimeMillis()
        val elapsed = now - lastTapAt
        if (elapsed in DOUBLE_TAP_MIN_GAP_MS..DOUBLE_TAP_WINDOW_MS) {
            LockController.lock(this)
            lastTapAt = 0L
            return
        }
        lastTapAt = now
    }

    override fun onInterrupt() = Unit

    companion object {
        private const val DOUBLE_TAP_MIN_GAP_MS = 60L
        private const val DOUBLE_TAP_WINDOW_MS = 400L
    }
}
