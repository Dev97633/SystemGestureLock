package com.dev.systemgesture.core

import android.accessibilityservice.AccessibilityService
import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.os.Build
import com.dev.systemgesture.admin.MyDeviceAdminReceiver

object LockController {

    fun lock(context: Context): Boolean {
        val dpm = context.getSystemService(Context.DEVICE_POLICY_SERVICE)
            as DevicePolicyManager
        
        val comp = ComponentName(
            context,
            MyDeviceAdminReceiver::class.java
        )

        if (dpm.isAdminActive(comp)) {
            dpm.lockNow()
            return true
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P && context is AccessibilityService) {
            return context.performGlobalAction(AccessibilityService.GLOBAL_ACTION_LOCK_SCREEN)
        }

        return false
    }
}
