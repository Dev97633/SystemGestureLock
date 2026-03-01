package com.dev.systemgesture.ui

import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.dev.systemgesture.admin.MyDeviceAdminReceiver
import com.dev.systemgesture.service.OverlayService
import android.app.admin.DevicePolicyManager

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
                        "Allow device admin so double tap can lock the screen."
                    )
                }
            )
            return
        }

        startOverlayService()
        hideLauncherIcon()
        finish()
    }

    private fun canStartFeature(): Boolean {
        val dpm = getSystemService(DEVICE_POLICY_SERVICE) as DevicePolicyManager
        val admin = ComponentName(this, MyDeviceAdminReceiver::class.java)
        return Settings.canDrawOverlays(this) && dpm.isAdminActive(admin)
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
