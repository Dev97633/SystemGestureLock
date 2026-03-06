package com.dev.systemgesture.service

import android.accessibilityservice.AccessibilityService
import android.content.Intent
import android.view.accessibility.AccessibilityEvent
import com.dev.systemgesture.core.LockController
import com.dev.systemgesture.ui.SetupActivity

class GestureAccessibilityService : AccessibilityService() {

    private var lastTapAt = 0L
    private var lastEventSignature = 0L

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        val safeEvent = event ?: return
        val eventType = safeEvent.eventType
        if (eventType != AccessibilityEvent.TYPE_TOUCH_INTERACTION_END &&
            eventType != AccessibilityEvent.TYPE_TOUCH_INTERACTION_START &&
            eventType != AccessibilityEvent.TYPE_VIEW_CLICKED
        ) {
            return
        }

        val now = safeEvent.eventTime
        val signature = (now shl 8) + eventType.toLong()
        if (signature == lastEventSignature) {
            return
        }
        lastEventSignature = signature

        val elapsed = now - lastTapAt
        if (elapsed in DOUBLE_TAP_MIN_GAP_MS..DOUBLE_TAP_WINDOW_MS) {
            val locked = LockController.lock(this)
            if (!locked) {
                val setupIntent = Intent(this, SetupActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                startActivity(setupIntent)
            }
            lastTapAt = 0L
            return
        }
        lastTapAt = now
    }

    override fun onInterrupt() = Unit

    companion object {
        private const val DOUBLE_TAP_MIN_GAP_MS = 40L
        private const val DOUBLE_TAP_WINDOW_MS = 750L
    }
}
