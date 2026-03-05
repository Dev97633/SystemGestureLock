package com.dev.systemgesture.service

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent
import com.dev.systemgesture.core.LockController

class GestureAccessibilityService : AccessibilityService() {

    private var lastTapAt = 0L
override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        val type = event?.eventType ?: return
        if (type != AccessibilityEvent.TYPE_TOUCH_INTERACTION_END &&
            type != AccessibilityEvent.TYPE_VIEW_CLICKED
        ) {
            return
        }

        val now = System.currentTimeMillis()
        if (now - lastTapAt in 1..DOUBLE_TAP_WINDOW_MS) {
            LockController.lock(this)
            lastTapAt = 0L
            return
        }
        lastTapAt = now
    }

    override fun onInterrupt() = Unit

    companion object {
        private const val DOUBLE_TAP_WINDOW_MS = 400L
    }
}
