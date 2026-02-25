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
