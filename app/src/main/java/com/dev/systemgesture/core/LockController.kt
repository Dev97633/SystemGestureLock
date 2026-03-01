package com.dev.systemgesture.core

import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import com.dev.systemgesture.admin.MyDeviceAdminReceiver

object LockController {

    fun lock(context: Context) {
        val dpm = context.getSystemService(Context.DEVICE_POLICY_SERVICE)
                as DevicePolicyManager

        val comp = ComponentName(
            context,
            MyDeviceAdminReceiver::class.java
        )

        if (dpm.isAdminActive(comp)) {
            dpm.lockNow()
        }
    }
}
